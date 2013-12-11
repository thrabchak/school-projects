<?php
	ob_start();
	session_start();
  $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
  if (!$conn) die("Cound not connect to database");       
  mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$item = $_POST['item'];
	$quantity = intval($_POST['quantity']);
	
	// Get num available
	$result = mysql_query("SELECT DISTINCT count FROM StockShelves WHERE item='$item';");
	if (!$result) die("Query1 failed!" . mysql_error());
	$row = mysql_fetch_row($result);
	$num_in_stock = intval($row[0]);
	echo "<br>".$item . "<br>" . $quantity . "<br>" . $num_in_stock . "<br>";
	

	// if we can update the database
	if($num_in_stock > $quantity and $num_in_stock > 0){

		// update GroceryShelves
		$new_onShelves_quant = $num_in_stock + $quantity;
		echo $new_onShelves_quant . "<br>";
		$result = mysql_query("UPDATE GroceryShelves SET GroceryShelves.count=($new_onShelves_quant) WHERE item='$item';");
		if (!$result) die("Query2 failed!" . mysql_error());
		
		// update StockShelves
		$new_inStock_quant = $num_in_stock - $quantity;
		$result = mysql_query("UPDATE StockShelves SET StockShelves.count=($new_inStock_quant) WHERE item='$item';");
		if (!$result) die("Query3 failed!" . mysql_error());
		
	}
	$url = "../stocker.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
