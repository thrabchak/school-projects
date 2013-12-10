<?php
	ob_start();
	session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$idnum = $_POST['cart'];
	$item = $_POST['item'];
	$quantity = $_POST['quantity'];

	/*
	echo $name;
	echo $address;
	echo $quantity
	*/
	
	// Check to see if the item is availiable
	$result = mysql_query("SELECT count FROM GroceryShelves WHERE item='$item';");
	if (!$result) 
	  die("Query (1) to show tuples from table failed!" . mysql_error());
	$row = mysql_fetch_row($result);

	if ($row[0] > $quantity){ // Reduce count on grocery shelves
		$temp = $row[0] - $quantity;
		$result = mysql_query("UPDATE GroceryShelves SET count=$temp WHERE item='$item';");
		if (!$result) 
	  		die("Query (2) to show tuples from table failed!" . mysql_error());
	}
	else { // $row[0] <= $quantity // remove item from grocery shelves
		$quantity = $row[0];
		$result = mysql_query("DELETE FROM GroceryShelves WHERE item='$item';");
		if (!$result) 
	  		die("Query (3) to show tuples from table failed!" . mysql_error());
	}
	

	// Check to see if the item is already in cart
	$result = mysql_query("SELECT EXISTS(SELECT 1 FROM Carts WHERE item='$item' AND cartID=$idnum);");
	if (!$result) 
	  die("Query (4) to show tuples from table failed!" . mysql_error());
	$row = mysql_fetch_row($result);

	if ($row[0] == 1){ // Item exists, add to its value
		$result = mysql_query("SELECT itemQuantity FROM Carts WHERE item='$item' AND cartID=$idnum;");
		if (!$result) 
	  		die("Query (5) to show tuples from table failed!" . mysql_error());
		$oldvalue = mysql_fetch_row($result);

		$value = $oldvalue[0] + $quantity;
		$result = mysql_query("UPDATE Carts SET itemQuantity=$value WHERE item='$item' AND cartID=$idnum;");
		if (!$result) 
	  		die("Query (6) to show tuples from table failed!" . mysql_error());
	}
	else{ // Item doesnt exist, make a new table entry
		// Create new table value
		$result = mysql_query("INSERT INTO Carts VALUES('$idnum', '$item', '$quantity');");
		if (!$result) 
	  		die("Query (7) to show tuples from table failed!" . mysql_error());
	}

	$url = "http://eg.bucknell.edu/~kmw023/cs305/jobs/customer.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
