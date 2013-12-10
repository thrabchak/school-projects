<?php
  session_start();
  header("Location: ". $_SESSION["url"] . $_POST["actions"]);
?>
