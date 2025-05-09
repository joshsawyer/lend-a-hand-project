<?php
// DB connection settings
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

// Query to join request info with user and resource names
$sql = "
    SELECT 
        r.Request_ID,
        r.User_ID,
        l.User_FName,
        l.User_LName,
        res.Resource_Name,
        r.Amount_Requested,
        r.Request_Bio,
        r.Date_Requested,
        r.Resource_ID,
        r.Amount_Received
    FROM request r
    JOIN login l ON r.User_ID = l.User_ID
    JOIN resources res ON r.Resource_ID = res.Resource_ID
    ORDER BY r.Date_Requested DESC
";

$result = $conn->query($sql);

$requests = [];

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $requests[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($requests);
$conn->close();
?>
