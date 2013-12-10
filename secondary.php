<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="./icon/icon.jpg">
    <!-- Bootstrap core CSS -->
    <link href="./css/bootstrap.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="./css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
     <link href="./css/navbar.css" rel="stylesheet">

    <title>Grocery Store</title>
	</head>

	<body>
		<!-- Static navbar -->
      <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">CS305 - Grocery Store</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="http://www.eg.bucknell.edu/~trh010/cs305/test.html">Login Page</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>

     <!--  <div class="jumbotron">
        <h1>Navbar example</h1>
        <p>This example is a quick exercise to illustrate how the default, static navbar and fixed to top navbar work. It includes the responsive CSS and HTML, so it also adapts to your viewport and device.</p>
        <p>
          <a class="btn btn-lg btn-primary" href="../../components/#navbar" role="button">View navbar docs &raquo;</a>
        </p>
      </div> -->
		<div class="container">
			<?php
				session_start();
				$conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
				if (!$conn) die("Cound not connect to database");			  
				mysql_select_db($_SESSION["database"]) or die("Unable to select database");

				switch ($_POST["job"]) {
			    case "admin":

			    case "manager":
			    	echo "<h2>Employees</h2>";
			    	$result = mysql_query("SELECT * FROM Employees");
					  if (!$result) 
					    die("Query to show tuples from table failed!" . mysql_error());

					  $fields_num = mysql_num_fields($result);
					  echo "<table class=\"table table-hover\" ><thead><tr>";
					  // Print headers
					  for($i = 0; $i < $fields_num; $i++) {
					      $field = mysql_fetch_field($result);
					      echo "<th>{$field->name}</th>";
					  }
					  echo "</tr></thead>\n<tbody>\n";
					  
					  // Print data
					  while($row = mysql_fetch_row($result)) {
					      echo "<tr>";
					      foreach($row as $cell)
					          echo "<td>$cell</td>";
					      echo "</tr>\n";
					  }
					  echo "</tbody>\n</table>";
					  mysql_free_result($result);
					  
			    case "cashier":
			    	echo "<h2>Checkout Log</h2>";
			    	$result = mysql_query("SELECT * FROM CheckedOut");
					  if (!$result) 
					    die("Query to show tuples from table failed!" . mysql_error());

					  $fields_num = mysql_num_fields($result);
					  echo "<table class=\"table table-hover\" ><thead><tr>";
					  // Print headers
					  for($i = 0; $i < $fields_num; $i++) {
					      $field = mysql_fetch_field($result);
					      echo "<th>{$field->name}</th>";
					  }
					  echo "</tr></thead>\n<tbody>\n";
					  
					  // Print data
					  while($row = mysql_fetch_row($result)) {
					      echo "<tr>";
					      foreach($row as $cell)
					          echo "<td>$cell</td>";
					      echo "</tr>\n";
					  }
					  echo "</tbody>\n</table>";
					  mysql_free_result($result);

			    case "stocker":
		    		echo "<h2>Items Available</h2>";
		    		$result = mysql_query("SELECT * FROM GroceryShelves");
					  if (!$result) 
					    die("Query to show tuples from table failed!" . mysql_error());

					  $fields_num = mysql_num_fields($result);
					  echo "<table class=\"table table-hover\" ><thead><tr>";
					  // Print headers
					  for($i = 0; $i < $fields_num; $i++) {
					      $field = mysql_fetch_field($result);
					      echo "<th>{$field->name}</th>";
					  }
					  echo "</tr></thead>\n<tbody>\n";
					  
					  // Print data
					  while($row = mysql_fetch_row($result)) {
					      echo "<tr>";
					      foreach($row as $cell)
					          echo "<td>$cell</td>";
					      echo "</tr>\n";
					  }
					  echo "</tbody>\n</table>";
					  mysql_free_result($result);
			   
			    default: # Customer
		        echo "<h2>Items</h2>";
		        $result = mysql_query("SELECT * FROM ItemList");
					  if (!$result) 
					    die("Query to show tuples from table failed!" . mysql_error());

					  $fields_num = mysql_num_fields($result);
					  echo "<table class=\"table table-hover\" ><thead><tr>";
					  // Print headers
					  for($i = 0; $i < $fields_num; $i++) {
					      $field = mysql_fetch_field($result);
					      echo "<th>{$field->name}</th>";
					  }
					  echo "</tr></thead>\n<tbody>\n";
					  
					  // Print data
					  while($row = mysql_fetch_row($result)) {
					      echo "<tr>";
					      foreach($row as $cell)
					          echo "<td>$cell</td>";
					      echo "</tr>\n";
					  }
					  echo "</tbody>\n</table>";
					  mysql_free_result($result);
		        break;
			  }
		    mysql_close($conn);
			?>
		</div> <!-- /container -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
	</body>
</html>	


