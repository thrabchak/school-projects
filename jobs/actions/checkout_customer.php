<?php
	ob_start();
	session_start();
  $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
  if (!$conn) die("Cound not connect to database");       
  mysql_select_db($_SESSION["database"]) or die("Unable to select database");
	
	if($_POST['cartID']!="" or $_POST['employeeID'] != ""){
		$cartID = intval($_POST['cartID']);
		$employeeID = intval($_POST['employeeID']);

		echo $cartID . "<br>";
		echo $employeeID . "<br>";

		$totalCost = 0;
		echo "totalcost = " . $totalCost . "<br>";
	
		//find total cost
		$result = mysql_query("SELECT itemQuantity, price from Carts JOIN ItemList ON Carts.item=ItemList.item WHERE cartID='$cartID';");
			if (!$result) die("Query to show tuples from table failed!" . mysql_error());
			$num_row = mysql_num_rows($result);

			for($i = 0; $i < $num_row; $i++) {
				$row = mysql_fetch_row($result);
				$totalCost = $totalCost + (intval($row[0]) * intval($row[1]));
				echo "totalcost". $i." = " . $totalCost . "<br>"; 
			}

		$result = mysql_query("INSERT INTO CheckedOut VALUES('$employeeID', '$cartID', '$totalCost');");
		if (!$result) die("Query failed!" . mysql_error());
	
		// Get id num
	
		$url = "../cashier.php";
		while (ob_get_status()) {ob_end_clean();}
	}
	header( "Location: $url" );
?>
