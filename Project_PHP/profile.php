<?php

header('Content-Type: application/json');

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// 1. Check if userId is provided
if (!isset($_GET['userId'])) {
    http_response_code(400); // Bad Request
    echo json_encode(["error" => "Missing userId"]);
    exit();
}

$userId = $_GET['userId'];


$user = "s2864063";
$pass = "s2864063";
$db = "d2864063";
$host = "127.0.0.1";

$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["error" => "Database connection failed"]);
    exit();
}

// 3. Prepare and execute SQL query
$stmt = $conn->prepare("SELECT User_FName, User_LName FROM user WHERE User_ID = ?");
$stmt->bind_param("i", $userId);
$stmt->execute();
$result = $stmt->get_result();

// 4. Check if user exists
if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    echo json_encode($user);  // {"fname":"John", "lname":"Doe", "email":"john@example.com"}
} else {
    http_response_code(404); // Not Found
    echo json_encode(["error" => "User not found"]);
}

$stmt->close();
$conn->close();
?>