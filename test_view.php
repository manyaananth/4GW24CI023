<?php
include 'db_config.php';

$sql = "SELECT recipe_name, cuisine, course FROM recipes LIMIT 10";
$result = $conn->query($sql);

echo "<h1>Ruchikarr Recipe Preview</h1>";
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo "<p><b>" . $row["recipe_name"]. "</b> - " . $row["cuisine"]. " (" . $row["course"]. ")</p>";
    }
} else {
    echo "0 results found in database";
}
$conn->close();
?>