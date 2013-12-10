<?php
	ob_start();
	session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$name = $_POST['cart'];
	$address = $_POST['item'];
	$quantity = $_POST['quantity'];

	/*
	echo $name;
	echo $address;
	echo $quantity
	*/
	
	// Create new table value
	$result = mysql_query("INSERT INTO Carts VALUES('$idnum', '$item', '$quantity');");
	
	$url = "http://eg.bucknell.edu/~kmw023/cs305/jobs/customer.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
