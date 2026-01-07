<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ruchikarr_db"; // Ensure this matches your database name in phpMyAdmin

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>