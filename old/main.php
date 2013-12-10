<!DOCTYPE html>
<html><head></head>
<body>
<?php 
  session_destroy();
  session_start();
  $user = $_POST["user"];
  $password = $_POST["pwd"];
  $database = "csci305_" . $user;
  $_SESSION["user"] = $user;
  $_SESSION["pwd"] = $password;
  $_SESSION["database"] = $database;
<<<<<<< HEAD:main.php

=======
  echo $_SESSION["user"] . "<br>" . $_SESSION["pwd"] . "<br>" . $_SESSION["database"] . "<br>";
>>>>>>> bc0d7cb4328f8aaa66074d49d3d8554560eb19b4:old/main.php
/*
  echo $_SESSION["user"];
  echo $_SESSION["pwd"];
  echo $_SESSION["database"];
*/

  $job = $_POST["job"];
  $url = "http://eg.bucknell.edu/~".$user."/cs305/";
  $_SESSION["url"] = $url;

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

?></body>
</html>
