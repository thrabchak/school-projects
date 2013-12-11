<?php
	ob_start();
	session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$name = $_POST['name'];
	$address = $_POST['address'];

	/*
	echo $name;
	echo $address;
	*/
	
	// Get id num
	$result = mysql_query("SELECT MAX(customerID) FROM Customers;");
	if (!$result) 
          die("Query failed!" . mysql_error());
	$row = mysql_fetch_row($result);
	$idnum = $row[0] + 1;

	// Get cart num
	$result = mysql_query("SELECT MAX(cartID) FROM Customers;");
	if (!$result) 
          die("Query failed!" . mysql_error());
	$row = mysql_fetch_row($result);
	$cartnum = $row[0] + 1;

	// Create new table value
	$result = mysql_query("INSERT INTO CustomerInfo VALUES('$idnum', '$name', '$address');");
	$result = mysql_query("INSERT INTO Customers VALUES('$cartnum', '$idnum');");
	
	$url = "../customer.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
