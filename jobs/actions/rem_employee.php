<?php
	ob_start();
	session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");

	$idnum = $_POST['idnum'];
	
	// Remove table
	$result = mysql_query("DELETE FROM Employees WHERE employeeID = $idnum;");
	
	$url = "../manager.php";
	while (ob_get_status()) 
	{
    		ob_end_clean();
	}

	header( "Location: $url" );
?>
