package com.example.lendahand;

public class Login {
    private int loginID;
    private String userID;
    private String userPassword;
    private String userSalt;
    private String userEmail;
    private String userPhone;
    private String userFName;
    private String userLName;

    public Login(int loginID, String userId, String userPassword, String userSalt,
                 String userEmail, String userPhone, String userFName, String userLName) {
        this.loginID = loginID;
        this.userID = userID;
        this.userPassword = userPassword;
        this.userSalt = userSalt;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userFName = userFName;
        this.userLName = userLName;
    }

    public int getLoginID() { return loginID; }
    public void setLoginID(int loginID) { this.loginID = loginID; }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    public String getUserSalt() { return userSalt; }
    public void setUserSalt(String userSalt) { this.userSalt = userSalt; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
    public String getUserFName() { return userFName; }
    public void setUserFName(String userFName) { this.userFName = userFName; }
    public String getUserLName() { return userLName; }
    public void setUserLName(String userLName) { this.userLName = userLName; }
}
