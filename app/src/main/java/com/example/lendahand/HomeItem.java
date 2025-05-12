package com.example.lendahand;

public class HomeItem {
    private String fullName;
    private int totalRequested;
    private int percentReceived;
    private String userID;
    private String userBio;

    public HomeItem(String fullName, int totalRequested, int percentReceived, String userID, String userBio) {
        this.fullName = fullName;
        this.totalRequested = totalRequested;
        this.percentReceived = percentReceived;
        this.userID = userID;
        this.userBio = userBio;

    }

    public String getFullName() { return fullName; }
    public int getPercentReceived() { return percentReceived;}
    public String getUserID() { return userID; }
    public int getAmountRequested() { return totalRequested; }
    public String getUserBio(){return userBio;}
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPercentReceived(int percentReceived) { this.percentReceived = percentReceived; }
    public void setUserID(String userID) { this.userID = userID; }
    public void setTotalRequested(int totalRequested) {  this.totalRequested = totalRequested; }

}

