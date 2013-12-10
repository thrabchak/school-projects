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

	// Compare name
	$matched = 0;
	$result = mysql_query("SELECT customerName, customerID FROM CustomerInfo;");
	if (!$result) 
	  die("Query to show tuples from table failed!" . mysql_error());
	$num_row = mysql_num_rows($result);

	for($i = 0; $i < $num_row; $i++) {
		$tablename = mysql_fetch_row($result);
		if ($tablename[0] == $name){
			$idnum = $tablename[1];
			$matched = 1;
		}
	}

	// Create new table value
	if ($matched == 0){
		$result = mysql_query("INSERT INTO CustomerInfo VALUES('$idnum', '$name', '$address');");
		if (!$result) 
          		die("Query failed!" . mysql_error());
	}

	$result = mysql_query("INSERT INTO Customers VALUES('$cartnum', '$idnum');");
	if (!$result) 
          die("Query failed!" . mysql_error());
	
	$url = "http://eg.bucknell.edu/~kmw023/cs305/jobs/customer.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
