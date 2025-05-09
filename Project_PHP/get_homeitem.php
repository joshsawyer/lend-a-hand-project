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
        concat(l.User_FName, ' ', l.User_LName) AS Full_Name,
        r.Amount_Requested,
        r.Amount_Received,
        r.User_ID,
        r.Request_ID,
        res.Resource_Name,
        ROUND(
        CASE
            WHEN r.Amount_Requested = 0 THEN 0
            ELSE (r.Amount_Received *100) / r.Amount_Requested
        END, 
        0) AS Percentage_Received
        FROM request r
        JOIN login l ON r.User_ID = l.User_ID
        JOIN resources res ON r.Resource_ID = res.Resource_ID
        ORDER BY r.Date_Requested DESC
        ";
$result = $conn->query($sql);

$homeitem = [];
if ($result-> num_rows > 0){
    while($row = $result->fetch_assoc()){
        $homeitem[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($homeitem);
$conn->close();

?>