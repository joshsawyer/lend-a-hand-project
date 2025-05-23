USE d2864063;

DROP TABLE IF EXISTS request_donation;
DROP TABLE IF EXISTS request;
DROP TABLE IF EXISTS donation;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS login;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
User_ID VARCHAR(13) PRIMARY KEY,
User_Location VARCHAR(100),
User_FName VARCHAR(100) NOT NULL,
User_LName VARCHAR(100) NOT NULL,
User_Email VARCHAR(100) NOT NULL,
User_Phone VARCHAR(15) NOT NULL
Avatar_URL VARCHAR(255),

);

CREATE TABLE login (
Login_ID INT PRIMARY KEY AUTO_INCREMENT,
User_ID VARCHAR(13) NOT NULL UNIQUE,
User_Password VARCHAR(255) NOT NULL,
User_Salt VARCHAR(50) NOT NULL,
FOREIGN KEY (User_ID) REFERENCES user(User_ID)
);

CREATE TABLE resources (
Resource_ID INT PRIMARY KEY AUTO_INCREMENT,
Resource_Name VARCHAR(100) NOT NULL
);

CREATE TABLE donation (
Donation_ID INT PRIMARY KEY AUTO_INCREMENT,
User_ID VARCHAR(13) NOT NULL,
Resource_ID INT NOT NULL,
Amount_Donated INT NOT NULL,
Date_Donated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (User_ID) REFERENCES user(User_ID),
FOREIGN KEY (Resource_ID) REFERENCES resources(Resource_ID)
);

CREATE TABLE request (
Request_ID INT PRIMARY KEY AUTO_INCREMENT,
User_ID VARCHAR(13) NOT NULL,
Resource_ID INT NOT NULL,
Amount_Requested INT NOT NULL,
Amount_Received INT DEFAULT 0,
Request_Bio TEXT NOT NULL,
Date_Requested TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (User_ID) REFERENCES user(User_ID),
FOREIGN KEY (Resource_ID) REFERENCES resources(Resource_ID)
);

INSERT INTO user (User_ID, User_Location, User_FName, User_LName, User_Email, User_Phone) VALUES
('9201011234081', 'Cape Town', 'John', 'Mokoena', 'volunteer@example.com', '0821234567'),
('8507074567082', 'Durban', 'Thandi', 'Ngcobo', 'mom@example.com', '0839876543'),
('0112318765089', 'Pretoria', 'Sipho', 'Zulu', 'student@example.com', '0843219876'),
('9912123456789', 'Polokwane', 'Nomsa', 'Dlamini', 'nomsa@example.com', '0723456789'),
('0210101123456', 'East London', 'Karabo', 'Nkosi', 'karabo@example.com', '0734567890');

INSERT INTO login (User_ID, User_Password, User_Salt) VALUES
('9201011234081', 'hashed_pw_1', 'salt_1'),
('8507074567082', 'hashed_pw_2', 'salt_2'),
('0112318765089', 'hashed_pw_3', 'salt_3'),
('9912123456789', 'hashed_pw_4', 'salt_4'),
('0210101123456', 'hashed_pw_5', 'salt_5');

INSERT INTO resources (Resource_Name) VALUES
('Tinned Fish'), -- 1
('Airtime Voucher (R12)'), -- 2
('Baby Formula'), -- 3
('Stationery Pack'), -- 4
('Blankets'), -- 5
('Sanitary Pads'), -- 6
('Stationery'), -- 7
('Canned Food'), -- 8
('Jackets'); -- 9

INSERT INTO donation (User_ID, Resource_ID, Amount_Donated) VALUES
('9201011234081', 1, 10), -- Tinned Fish
('0112318765089', 4, 5); -- Stationery Pack

INSERT INTO request (User_ID, Resource_ID, Amount_Requested, Amount_Received, Request_Bio) VALUES
('8507074567082', 1, 5, 0, 'Looking for tinned food for my children.'),
('8507074567082', 3, 2, 0, 'Need baby formula urgently.'),
('9912123456789', 1, 3, 0, 'I need food assistance this week.'),
('0210101123456', 2, 1, 0, 'Need airtime to apply for jobs.'),
('0210101123456', 3, 1, 0, 'Need baby formula for my newborn.'),
('9201011234081', 5, 10, 0, 'Need blankets for winter.'),
('8507074567082', 6, 15, 0, 'Requesting sanitary pads for local schoolgirls.'),
('0112318765089', 7, 20, 0, 'Stationery needed for tutoring group.'),
('9201011234081', 8, 5, 0, 'Extra canned food for distribution.'),
('0112318765089', 9, 7, 0, 'Warm jackets for community shelter.');

UPDATE request
SET Amount_Received = FLOOR(RAND() * (Amount_Requested + 1));

CREATE TABLE request_donation (
RequestDonation_ID INT PRIMARY KEY AUTO_INCREMENT,
Request_ID INT NOT NULL,
Donor_ID VARCHAR(13) NOT NULL,
Resource_ID INT NOT NULL,
Amount_Donated INT NOT NULL,
Date_Donated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (Request_ID) REFERENCES request(Request_ID),
FOREIGN KEY (Donor_ID) REFERENCES user(User_ID),
FOREIGN KEY (Resource_ID) REFERENCES resources(Resource_ID)
);

INSERT INTO request_donation (Request_ID, Donor_ID, Resource_ID, Amount_Donated) VALUES
(1, '9201011234081', 1, 3),
(2, '9201011234081', 3, 1),
(3, '9201011234081', 1, 2),
(5, '0112318765089', 3, 1),
(7, '9201011234081', 6, 2),
(8, '9201011234081', 8, 4),
(10, '0112318765089', 9, 3);