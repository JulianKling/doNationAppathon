<?php

    $id = $_GET['id'];
	$time_collected = time();;
	$taker = $_GET['taker'];
	
	$dbhost   = "localhost"; 
	$dblogin  = "casaro_coupon";
	$dbpasswd = "Bqyt042%"; 
	$dbname   = "casaro_data"; 
	
	$output = "<result>\n";
	$output .= "<action>collect</action>\n";
	
	mysql_connect($dbhost, $dblogin, $dbpasswd) or die(mysql_error()); 
	mysql_select_db($dbname) or die(mysql_error()); 
	
    if (id != "" && $taker != "")
	{
		$sqlUpdate = "UPDATE `donation` 
			SET `time_collected` = '" . strip_tags(mysql_escape_string($time_collected)) . "' , 
			`taker` = '" . strip_tags(mysql_escape_string($taker)) . "' 
			WHERE `id` = '" . strip_tags(mysql_escape_string($id)) . "'";

        mysql_query($sqlUpdate);
		
		$sqlGet = "SELECT * FROM `donation`
				WHERE `id` = '" . strip_tags(mysql_escape_string($id)) . "'
				LIMIT 100";

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
	}
	
	$output .= "</result>";

	$xml = new SimpleXMLElement($output);
	echo $xml->asXML();
?>