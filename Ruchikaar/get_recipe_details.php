<?php
include 'db_connect.php';
$id = intval($_GET['id']);
$sql = "SELECT *, sr_no as recipeId, recipe_name as recipeName FROM recipes WHERE sr_no = $id";
$result = $conn->query($sql);
header('Content-Type: application/json');
echo json_encode($result->fetch_assoc());
?>