<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../icon/icon.jpg">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="../css/navbar.css" rel="stylesheet">

  <title>Grocery Store</title>
  </head>

  <body>
    <!-- Static navbar -->
      <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <a class="navbar-brand" href="../index.html">CS305 - Grocery Store</a>
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Change View<b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="admin.php">Admin</a></li>
                <li><a href="customer.php">Customer</a></li>
                <li><a href="manager.php">Manager</a></li>
                <li><a href="cashier.php">Cashier</a></li>
                <li><a href="stocker.php">Stocker</a></li>
              </ul>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="../index.html">Login Page</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>

      <div class="jumbotron">
        <h1>Administrator</h1>
        <p>This is the administrator view. From here you can view all tables in the grocery store database</p>
      </div>
    <div class="container">
      <?php
        session_start();
        $conn = mysql_connect("db.eg.bucknell.edu",$_SESSION["user"],$_SESSION["pwd"]);
        if (!$conn) die("Cound not connect to database");       
        mysql_select_db($_SESSION["database"]) or die("Unable to select database");
      ?>
      <h2>Customers</h2>
      <?php
        $result = mysql_query("SELECT cartID,Customers.customerID,customerName,customerAddress FROM Customers INNER JOIN CustomerInfo ON Customers.CustomerID=CustomerInfo.CustomerID;");
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
      ?>
      <h2>Employees</h2>
      <?php
        $result = mysql_query("SELECT * FROM Employees;");
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
      ?>
			<h2>Active Carts</h2>
      <?php
        $result = mysql_query("Select cartID, customerName, item, itemQuantity FROM (
    			SELECT A.cartID, item, itemQuantity, customerID 	
    			FROM (SELECT * FROM Customers 		                                         
          WHERE cartID NOT IN (SELECT cartID 
                               FROM
                               CheckedOut)) A 
    			LEFT JOIN Carts B 
    			ON A.cartID = B.cartID 
					ORDER BY A.cartID)C
					JOIN
					CustomerInfo D
					ON C.customerID=D.customerID
					ORDER BY cartID;");
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
      ?>
      <h2>All Carts Ever</h2>
      <p><b>note: </b>empty carts are not displayed here</p>
      <?php 
        $result = mysql_query("SELECT * FROM Carts ORDER BY cartID ASC;");
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
      ?>
      <h2>Checkout Log</h2>
      <p><b>note: </b>the cartID (which is the primary key of this table) is the second column</p>
      <?php
        $result = mysql_query("SELECT * FROM CheckedOut ORDER BY cartID ASC;");
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
      ?>
      <h2>Items In Inventory</h2>
      <?php
        $result = mysql_query("SELECT * FROM StockShelves");
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
      ?>
      <h2>Items On Shelves</h2>
      <?php
        $result = mysql_query("SELECT * FROM GroceryShelves INNER JOIN ItemList ON GroceryShelves.item=ItemList.Item;");
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
        mysql_close($conn);
      ?>
    </div> <!-- /container -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
  </body>
</html> 


