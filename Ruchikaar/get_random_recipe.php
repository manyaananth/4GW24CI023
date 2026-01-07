<?php
include 'db_connect.php';

// Set header to JSON so the JavaScript can read it
header('Content-Type: application/json');

// Using 'sr_no' as the ID based on your database screenshot
$sql = "SELECT sr_no as recipeId, recipe_name as recipeName, region, 
        total_time_mins as totalTimeMins, servings, cuisine, course, diet 
        FROM recipes ORDER BY RAND() LIMIT 10";

$result = $conn->query($sql);
$recipes = [];

if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $recipes[] = $row;
    }
}

echo json_encode($recipes);
$conn->close();
?>