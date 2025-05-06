<?php
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

$sql = "
    SELECT 
        u.User_ID,
        u.User_Bio,
        u.Total_Donated,
        u.Total_Received,
        l.User_FName,
        l.User_LName,
        l.User_Email,
        l.User_Phone
    FROM user u
    JOIN login l ON u.User_ID = l.User_ID
";
$result = $conn->query($sql);

$users = [];
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $users[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($users);
$conn->close();
?>