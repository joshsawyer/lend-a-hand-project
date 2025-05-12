<?php
header('Content-Type: application/json');
$host = 'localhost';
$db = 'd2864063';
$user = 's2864063';
$pass = 's2864063';

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    echo json_encode(['success' => false, 'message' => 'Database connection failed.']);
    exit;
}

// Fetch POST data
$donor_id = $_POST['donor_id'] ?? '';
$request_id = $_POST['request_id'] ?? '';
$amount = $_POST['amount'] ?? '';

// Validate input
if (empty($donor_id) || empty($request_id) || empty($amount)) {
    echo json_encode(['success' => false, 'message' => 'Missing data.']);
    exit;
}

$amount = intval($amount);

// Fetch resource ID from the request
$stmt = $conn->prepare("SELECT Resource_ID FROM request WHERE Request_ID = ?");
$stmt->bind_param("i", $request_id);
$stmt->execute();
$stmt->bind_result($resource_id);
if (!$stmt->fetch()) {
    echo json_encode(['success' => false, 'message' => 'Invalid request ID.']);
    exit;
}
$stmt->close();

// Insert into donation table
$stmt = $conn->prepare("INSERT INTO donation (User_ID, Resource_ID, Amount_Donated) VALUES (?, ?, ?)");
$stmt->bind_param("sii", $donor_id, $resource_id, $amount);
if (!$stmt->execute()) {
    echo json_encode(['success' => false, 'message' => 'Failed to record donation.']);
    exit;
}
$stmt->close();

// Insert into request_donation table
$stmt = $conn->prepare("INSERT INTO request_donation (Request_ID, Donor_ID, Resource_ID, Amount_Donated) VALUES (?, ?, ?, ?)");
$stmt->bind_param("isii", $request_id, $donor_id, $resource_id, $amount);
if (!$stmt->execute()) {
    echo json_encode(['success' => false, 'message' => 'Failed to link donation to request.']);
    exit;
}
$stmt->close();

// Update amount received
$stmt = $conn->prepare("UPDATE request SET Amount_Received = Amount_Received + ? WHERE Request_ID = ?");
$stmt->bind_param("ii", $amount, $request_id);
if (!$stmt->execute()) {
    echo json_encode(['success' => false, 'message' => 'Failed to update request.']);
    exit;
}
$stmt->close();

// Success
echo json_encode(['success' => true, 'message' => 'Donation recorded successfully.']);
?>
