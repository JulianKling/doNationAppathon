<?php

	$id = $_GET['id'];
	$organization = $_GET['organization'];
	$item = $_GET['item'];
	
	$dbhost   = "localhost"; 
	$dblogin  = "casaro_coupon";
	$dbpasswd = "Bqyt042%"; 
	$dbname   = "casaro_data"; 
	
	$output = "<result>\n";
	$output .= "<action>get_info</action>\n";
	
	mysql_connect($dbhost, $dblogin, $dbpasswd) or die(mysql_error()); 
	mysql_select_db($dbname) or die(mysql_error()); 
	
    if ($organization != "" && $item != "")
	{
		$sqlGet = "SELECT * FROM `donation`
			WHERE `organization` = '" . strip_tags(mysql_escape_string($organization)) . "'
			AND `item` = '" . strip_tags(mysql_escape_string($item)) . "'
			ORDER BY `time_donated` DESC
			LIMIT 100";
	}	
	else if ($organization != "")
	{
		$sqlGet = "SELECT * FROM `donation`
			WHERE `organization` = '" . strip_tags(mysql_escape_string($organization)) . "'
			ORDER BY `time_donated` DESC
			LIMIT 100";
	}
	else if ($id != "")
	{
		$sqlGet = "SELECT * FROM `donation`
			WHERE `id` = '" . strip_tags(mysql_escape_string($id)) . "'
			LIMIT 1";
	}
	else
	{
		$sqlGet = "SELECT * FROM `donation`
			WHERE `taker` = '' 
			ORDER BY `time_donated` DESC
			LIMIT 100";
	}

	$result = mysql_query($sqlGet); 
	mysql_close();
		 
	for($i = 0 ; $i < mysql_num_rows($result) ; $i++)
	{
		$row = mysql_fetch_assoc($result);
	
		$output .= "<entity> \n";
		$output .= "	<id>" . trim($row['id']) . "</id> \n";
		$output .= "	<item>" . trim($row['item']) . "</item> \n";
		$output .= "	<percent>" . trim($row['percent']) . "</percent> \n";
		$output .= "	<organization>" . trim($row['organization']) . "</organization> \n";
		$output .= "	<time_donated>" . trim($row['time_donated']) . "</time_donated> \n";
		$output .= "	<time_collected>" . trim($row['time_collected']) . "</time_collected> \n";
		$output .= "	<giver>" . trim($row['giver']) . "</giver> \n";
		$output .= "	<taker>" . trim($row['taker']) . "</taker> \n";
		$output .= "</entity> \n";
	}
	
	$output .= "</result>";

	$xml = new SimpleXMLElement($output);
	echo $xml->asXML();
?>