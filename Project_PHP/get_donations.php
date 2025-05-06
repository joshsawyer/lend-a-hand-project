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
        d.Donation_ID,
        d.User_ID,
        l.User_FName,
        l.User_LName,
        r.Resource_Name,
        d.Amount_Donated,
        d.Date_Donated
    FROM donation d
    JOIN login l ON d.User_ID = l.User_ID
    JOIN resources r ON d.Resource_ID = r.Resource_ID
    ORDER BY d.Date_Donated DESC
";
$result = $conn->query($sql);

$donations = [];
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $donations[] = $row;
    }
}

header('Content-Type: application/json');
echo json_encode($donations);
$conn->close();
?>
