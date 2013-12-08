<?php 
  session_start();
  $user = $_POST["user"];
  $password = $_POST["pwd"];
  $database = "csci305_" . $_POST["user"];
  $_SESSION["user"] = $user;
  $_SESSION["pwd"] = $pwd;
  $_SESSION["database"] = $database;
/*
  echo $_SESSION["user"];
  echo $_SESSION["pwd"];
  echo $_SESSION["database"];
*/

  $job = $_POST["job"];
  $url = "http://eg.bucknell.edu/~kmw023/cs305/";

  echo "<h1>What would you like to do?</h1>";
  echo "<form action=\"handler.php\" method=\"POST\">";
  echo "<select name=\"actions\" size=\"1\" Font size=\"2\">";
  switch ($job) {
    case "admin":
        echo "<option value = \"connect.php\" >See Database</option>";
    case "manager":
        echo "<option value = \"connect.php\" >See Employee Information</option>";
    case "cashier":
        echo "<option value = \"connect.php\" >See Checkout Activity</option>";
    case "stocker":
        echo "<option value = \"connect.php\" >See Items Availiable</option>";
        break;
    default: # Customer
        echo "<option value = \"connect.php\" >Add Items to Cart</option>";
        echo "<option value = \"connect.php\" >Checkout</option>";
  }
  echo "</select>";
  echo "<input type=\"submit\" value=\"Submit\">";
  echo "</form>";

?>
