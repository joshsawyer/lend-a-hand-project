package com.example.lendahand;

public class HomeItem {
    private String fullName;
    private int totalRequested;
    private int percentReceived;
    private String userID;
    private String userLocation;

    public HomeItem(String fullName, int totalRequested, int percentReceived, String userID, String userLocation) {
        this.fullName = fullName;
        this.totalRequested = totalRequested;
        this.percentReceived = percentReceived;
        this.userID = userID;
        this.userLocation = userLocation;

    }

    public String getFullName() { return fullName; }
    public int getPercentReceived() { return percentReceived;}
    public String getUserID() { return userID; }
    public int getAmountRequested() { return totalRequested; }
    public String getUserLocation(){return userLocation;}
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPercentReceived(int percentReceived) { this.percentReceived = percentReceived; }
    public void setUserID(String userID) { this.userID = userID; }
    public void setTotalRequested(int totalRequested) {  this.totalRequested = totalRequested; }

}

