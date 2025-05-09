USE d2864063;
CREATE TABLE user (
    User_ID VARCHAR(13) PRIMARY KEY,
    User_Bio TEXT,
    Total_Donated INT DEFAULT 0,
    Total_Received INT DEFAULT 0
);
CREATE TABLE login (
    Login_ID INT PRIMARY KEY AUTO_INCREMENT,
    User_ID VARCHAR(13) NOT NULL,
    User_Password VARCHAR(50) NOT NULL,
    User_Salt VARCHAR(50) NOT NULL,
    User_Email VARCHAR(100) NOT NULL,
    User_Phone VARCHAR(15) NOT NULL,
    User_FName VARCHAR(100) NOT NULL,
    User_LName VARCHAR(100) NOT NULL,
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
    Request_Bio TEXT NOT NULL,
    Date_Requested TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (User_ID) REFERENCES user(User_ID),
    FOREIGN KEY (Resource_ID) REFERENCES resources(Resource_ID)
);

-- Insert into user table
INSERT INTO user (User_ID, User_Bio, Total_Donated, Total_Received) VALUES
('9201011234081', 'I am a community volunteer from Cape Town.', 0, 0),
('8507074567082', 'Mother of three in need of food assistance.', 0, 0),
('0112318765089', 'Student offering extra stationery.', 0, 0);

-- Insert into login table
INSERT INTO login (User_ID, User_Password, User_Salt, User_Email, User_Phone, User_FName, User_LName) VALUES
('9201011234081', 'hashed_pw_1', 'salt_1', 'volunteer@example.com', '0821234567', 'John', 'Mokoena'),
('8507074567082', 'hashed_pw_2', 'salt_2', 'mom@example.com', '0839876543', 'Thandi', 'Ngcobo'),
('0112318765089', 'hashed_pw_3', 'salt_3', 'student@example.com', '0843219876', 'Sipho', 'Zulu');

-- Insert into resources table
INSERT INTO resources (Resource_Name) VALUES
('Tinned Fish'),
('Airtime Voucher (R12)'),
('Baby Formula'),
('Stationery Pack');

-- Insert into donation table
INSERT INTO donation (User_ID, Resource_ID, Amount_Donated) VALUES
('9201011234081', 1, 10), -- 10 tins of fish
('0112318765089', 4, 5);  -- 5 stationery packs

-- Insert into request table
INSERT INTO request (User_ID, Resource_ID, Amount_Requested, Request_Bio) VALUES
('8507074567082', 1, 5, 'Looking for tinned food for my children.'),
('8507074567082', 3, 2, 'Need baby formula urgently.');



ALTER TABLE request
ADD Amount_Received INT DEFAULT 0;