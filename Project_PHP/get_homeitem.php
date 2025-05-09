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
        CONCAT(u.User_FName, ' ', u.User_LName) AS Full_Name,
        u.User_ID,
        SUM(r.Amount_Requested) AS Total_Requested,
        SUM(r.Amount_Received) AS Total_Received,
        ROUND(
            CASE
                WHEN SUM(r.Amount_Requested) = 0 THEN 0
                ELSE (SUM(r.Amount_Received) * 100) / SUM(r.Amount_Requested)
            END, 
            0
        ) AS Percentage_Received
    FROM request r
    JOIN user u ON r.User_ID = u.User_ID
    WHERE r.Amount_Received < r.Amount_Requested
    GROUP BY r.User_ID
    ORDER BY MAX(r.Date_Requested) DESC
";

$result = $conn->query($sql);
$homeitem = [];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $homeitem[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($homeitem);
$conn->close();
?>
