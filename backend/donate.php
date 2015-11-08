<?php

    $id = rand(10000000, 99999999);
	$item = $_GET['item'];
	$percent = $_GET['percent'];
	$organization = $_GET['organization'];
	$time_donated = time();
	$time_collected = 0;
	$giver = $_GET['giver'];
	$taker = "";
	
	$dbhost   = "localhost"; 
	$dblogin  = "casaro_coupon";
	$dbpasswd = "Bqyt042%"; 
	$dbname   = "casaro_data"; 
	
	$output = "<result>\n";
	$output .= "<action>donate</action>\n";
	
	mysql_connect($dbhost, $dblogin, $dbpasswd) or die(mysql_error()); 
	mysql_select_db($dbname) or die(mysql_error()); 
	
    if ($item != "" && $percent != "" && $percent != "" && $giver != "")
	{
		$sqlInsert = "INSERT INTO `donation` (
			`id` ,
			`item` ,
			`percent` ,
			`organization` ,
			`time_donated` ,
			`time_collected` ,
			`giver` ,
			`taker`
		)
			VALUES (
			'".strip_tags(mysql_escape_string($id))."', 
			'".strip_tags(mysql_escape_string($item))."',
			'".strip_tags(mysql_escape_string($percent))."',
			'".strip_tags(mysql_escape_string($organization))."', 
			'".strip_tags(mysql_escape_string($time_donated))."', 
			'".strip_tags(mysql_escape_string($time_collected))."', 
			'".strip_tags(mysql_escape_string($giver))."', 
			'".strip_tags(mysql_escape_string($taker))."'
		)";

        mysql_query($sqlInsert);
		mysql_close();
	
		$output .= "<entity> \n";
		$output .= "	<id>" . trim($id) . "</id> \n";
		$output .= "	<item>" . trim($item) . "</item> \n";
		$output .= "	<percent>" . trim($percent) . "</percent> \n";
		$output .= "	<organization>" . trim($organization) . "</organization> \n";
		$output .= "	<time_donated>" . trim($time_donated) . "</time_donated> \n";
		$output .= "	<time_collected>" . trim($time_collected) . "</time_collected> \n";
		$output .= "	<giver>" . trim($giver) . "</giver> \n";
		$output .= "	<taker>" . trim($taker) . "</taker> \n";
		$output .= "</entity> \n";
	}
	
	$output .= "</result>";

	$xml = new SimpleXMLElement($output);
	echo $xml->asXML();
?>