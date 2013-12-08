<?php 
  $user = "kmw023";
  $password = "iexeipiM";
  $database = "test";
  $table = $_POST["table"];
  
  $conn = mysql_connect("db.eg.bucknell.edu",$user,$password);
  if (!$conn) die("Cound not connect to database");
  
  mysql_select_db($database) or die("Unable to select database");
  
  $result = mysql_query("SELECT * FROM {$table}");
  if (!$result) 
    die("Query to show tuples from table failed!" . mysql_error());

  $fields_num = mysql_num_fields($result);
  echo "<h1>Table: {$table}</h1>";
  echo "<table border='1'><tr>";
  // Print headers
  for($i = 0; $i < $fields_num; $i++) {
      $field = mysql_fetch_field($result);
      echo "<th>{$field->name}</th>";
  }
  echo "</tr>\n";
  
  // Print data
  while($row = mysql_fetch_row($result)) {
      echo "<tr>";
      foreach($row as $cell)
          echo "<td>$cell</td>";
      echo "</tr>\n";
  }
  mysql_free_result($result);
  mysql_close($conn);
?>
