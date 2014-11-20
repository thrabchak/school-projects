<?php
	ob_start();
	session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$job = $_POST['job'];
	$name = $_POST['name'];
	$address = $_POST['address'];

	/*
	echo $job;
	echo $name;
	echo $address;
	*/
	
	// Get id num
	$result = mysql_query("SELECT MAX(employeeID) FROM Employees;");
	if (!$result) 
          die("Query failed!" . mysql_error());
	$row = mysql_fetch_row($result);
	$idnum = $row[0] + 1;

	// Create new table value
	$result = mysql_query("INSERT INTO Employees VALUES('$idnum', '$name', '$address', '$job');");
	
	$url = "../manager.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
