<?php
  session_start();
  header("Location: ". $url . $_POST["actions"]);
?>
