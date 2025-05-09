package com.example.lendahand;

import java.sql.Timestamp;

public class Request {
    private int requestID;
    private String userID;
    private int resourceID;
    private int amountRequested;
    private String requestBio;
    private Timestamp dateRequested;
    private int amountReceived;

    public Request(int requestID, String userID, int resourceID, int amountRequested,
                   String requestBio, Timestamp dateRequested, int amountReceived) {
        this.requestID = requestID;
        this.userID = userID;
        this.resourceID = resourceID;
        this.amountRequested = amountRequested;
        this.requestBio = requestBio;
        this.dateRequested = dateRequested;
        this.amountReceived = amountReceived;
    }
    public int getRequestID() { return requestID; }
    public void setRequestID(int requestID) { this.requestID = requestID; }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) { this.resourceID = resourceID; }
    public int getAmountRequested() { return amountRequested; }
    public void setAmountRequested(int amountRequested) { this.amountRequested = amountRequested; }
    public String getRequestBio() { return requestBio; }
    public void setRequestBio(String requestBio) { this.requestBio = requestBio; }
    public Timestamp getDateRequested() { return dateRequested; }
    public void setDateRequested(Timestamp dateRequested) { this.dateRequested = dateRequested; }
    public int getAmountReceived() { return amountReceived; }
    public void setAmountReceived(int amountReceived) { this.amountReceived = amountReceived; }
}
