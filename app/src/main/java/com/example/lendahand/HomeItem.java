package com.example.lendahand;

public class HomeItem {
    private String fullName;
    private int amountRequested;
    private int percentReceived;
    private int requestID;

    private String userID;
    private String resourceName;

    public HomeItem(String fullName, int amountRequested, int percentReceived, String userID, String resourceName, int requestID) {
        this.fullName = fullName;
        this.amountRequested = amountRequested;
        this.percentReceived = percentReceived;
        this.userID = userID;
        this.resourceName = resourceName;
        this.requestID = requestID;
    }

    public String getFullName() { return fullName; }
    public int getAmountRequested() { return amountRequested; }
    public int getPercentReceived() { return percentReceived;}
    public String getUserID() { return userID; }
    public String getResourceName() { return resourceName; }
    public int getRequestID() { return requestID;}

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAmountRequested(int amountRequested) { this.amountRequested = amountRequested; }
    public void setPercentReceived(int percentReceived) { this.percentReceived = percentReceived; }
    public void setUserID(String userID) { this.userID = userID; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
}

