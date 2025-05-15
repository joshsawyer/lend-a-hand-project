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
        u.User_Bio,
        CONCAT(u.User_FName, ' ', u.User_LName) AS Full_Name,
        SUM(filtered.Amount_Requested) AS Total_Requested,
        SUM(filtered.Amount_Received) AS Total_Received
    FROM (
        SELECT *
        FROM request
        WHERE Amount_Requested > Amount_Received
    ) AS filtered
    JOIN user u ON filtered.User_ID = u.User_ID
";

if (!empty($search)) {
    $sql .= " WHERE CONCAT(u.User_FName, ' ', u.User_LName) LIKE '$search%'";
}

$sql .= " GROUP BY u.User_ID
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
