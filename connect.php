
<?php 
  session_start();
  $user = $_SESSION["user"];
  $password = $_SESSION["pwd"];
  $database = $_SESSION["database"];
  $conn = mysql_connect("db.eg.bucknell.edu",$user,$password);

/*
  echo $_SESSION["user"];
  echo $_SESSION["pwd"];
  echo $_SESSION["database"];
*/
  if (!$conn) 
     die("Cound not connect to database");

  mysql_select_db($database) or die("Unable to select database");

  $result = mysql_query("SHOW TABLES");
  if (!$result) 
    die("Query to show tables failed");

  $num_row = mysql_num_rows($result);
  
  echo "<h1>Choose one table:</h1>";
  echo "<form action=\"showtable.php\" method=\"post\">";
  echo "<select name=\"table\" size=\"1\" Font size=\"2\">";
  for($i = 0; $i < $num_row; $i++) {
      $tablename = mysql_fetch_row($result);
      echo "<option value = \"{$tablename[0]}\" >{$tablename[0]}</option>";
  }
  echo "</select>";
  echo "<div><input type=\"submit\" value=\"submit\"></div>";
  echo "</form>";
  
  mysql_free_result($result);
  mysql_close($conn);

?>

