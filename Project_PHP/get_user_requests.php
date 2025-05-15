<?php
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);

if ($conn -> connect_error){
    die(json_encode(["error" => "connection failed: " . $conn->error]));
}

if (!isset($_GET['userID'])) {
    die(json_encode(["error" => "Missing userID"]));
}

$userID = $_GET['userID'];

$response = [];

$bioQuery = "SELECT User_Bio FROM user WHERE User_ID = ?";
$bioStmt = $conn->prepare($bioQuery);
$bioStmt->bind_param("s", $userID);
$bioStmt->execute();
$bioResult = $bioStmt->get_result();

if ($bioRow = $bioResult->fetch_assoc()) {
    $response['User_Bio'] = $bioRow['User_Bio'];
} else {
    $response['User_Bio'] = null;
}
$bioStmt->close();
$requestQuery = "
    SELECT 
    r.Request_ID,
    r.Resource_ID,
    r.Amount_Requested,
    r.Amount_Received,
    r.Request_Bio,
    res.Resource_Name
FROM request r
JOIN resources res ON r.Resource_ID = res.Resource_ID
WHERE r.User_ID = ?
AND r.Amount_Requested > r.Amount_Received
ORDER BY r.Date_Requested ASC;
";

$requestStmt = $conn->prepare($requestQuery);
$requestStmt->bind_param("s", $userID);
$requestStmt->execute();
$requestResult = $requestStmt->get_result();
$requestStmt->close();

$requests = [];
while ($row = $requestResult->fetch_assoc()){
    $requests[] = $row;
}

$response['Requests'] = $requests;

header('Content-Type: application/json');
echo json_encode($response);
$conn->close();
?>