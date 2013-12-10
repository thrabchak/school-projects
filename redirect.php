<?php
  session_start();
  $user = $_POST["user"];
  $password = $_POST["pwd"];
  $database = "csci305_" . $user;
  $_SESSION["user"] = $user;
  $_SESSION["pwd"] = $password;
  $_SESSION["database"] = $database;
  $job = $_POST["job"];
  $url = "http://eg.bucknell.edu/~trh010/cs305/";
  switch ($job) {
    case "admin":
      header("Location: ". "./jobs/admin.php");
      break;
    case "manager":
      header("Location: ". "./jobs/manager.php");
      break;
    case "cashier":
      header("Location: ". "./jobs/cashier.php");
      break;
    case "stocker":
      header("Location: ". "./jobs/stocker.php");
      break;
    case "customer":
      header("Location: ". "./jobs/customer.php");
      break;
    default: #unknown - something went wrong
      header("Location: ". "sorry.html");
      break;
  }
?>
