<?php
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

$search = isset($_GET['search']) ? $conn->real_escape_string($_GET['search']) : '';

$sql = "
    SELECT
        u.User_ID,
        CONCAT(u.User_FName, ' ', u.User_LName) AS Full_Name,
        SUM(r.Amount_Requested) AS Total_Requested,
        SUM(r.Amount_Received) AS Total_Received
    FROM request r
    JOIN user u ON r.User_ID = u.User_ID
";

if (!empty($search)) {
    $sql .= " WHERE CONCAT(u.User_FName, ' ', u.User_LName) LIKE '$search%'";
}

$sql .= " GROUP BY u.User_ID
        HAVING Total_Received < Total_Requested
        ORDER BY u.User_FName";

$result = $conn->query($sql);

$data = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($data);
$conn->close();
?>
