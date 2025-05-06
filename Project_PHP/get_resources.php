<?php
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

$sql = "SELECT Resource_ID, Resource_Name FROM resources";
$result = $conn->query($sql);

$resources = [];
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $resources[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($resources);
$conn->close();
?>
