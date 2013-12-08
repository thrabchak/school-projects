<?php 
  $user = $_POST["user"];
  $password = $_POST["pwd"];
  $database = "csci305_" . $_POST["user"];
  $job = $_POST["job"];
  echo "<h1>What would you like to do?</h1>";
  echo "<form action=\"connect.php\" <form method=\"POST\">";
  echo "<input type=\"hidden\" name=\"user\" value=\"{$user}\">";
  echo "<input type=\"hidden\" name=\"pwd\" value=\"{$pwd}\">";
  echo "<input type=\"hidden\" name=\"database\" value=\"{$database}\">";
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

?>
