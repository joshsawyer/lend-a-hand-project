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
    User_ID VARCHAR(13) NOT NULL,
    Resource_Name VARCHAR(100) NOT NULL,
    FOREIGN KEY (User_ID) REFERENCES user(User_ID)
);
CREATE TABLE donation (
    Donation_ID INT PRIMARY KEY AUTO_INCREMENT,
    User_ID VARCHAR(13) NOT NULL,
    Amount_Donated INT NOT NULL,
    Date_Donated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (User_ID) REFERENCES user(User_ID)
);
CREATE TABLE request (
    Request_ID INT PRIMARY KEY AUTO_INCREMENT,
    User_ID VARCHAR(13) NOT NULL,
    Amount_Requested INT NOT NULL,
    Request_Bio TEXT NOT NULL,
    Date_Requested TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (User_ID) REFERENCES user(User_ID)
);