package com.example.lendahand;

//Please feel free to alter anything here this is just a basic setup for the user class so I can get the GUI working
public class User {
    private String userID;
    private String userBio;
    private int totalDonated;
    private int totalReceived;

    public User(String userID, String userBio, int totalDonated, int totalReceived) {
        this.userID = userID;
        this.userBio = userBio;
        this.totalDonated = totalDonated;
        this.totalReceived = totalReceived;
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getUserBio() { return userBio; }
    public void setUserBio(String userBio) { this.userBio = userBio; }
    public int getTotalDonated() { return totalDonated; }
    public void setTotalDonated(int totalDonated) { this.totalDonated = totalDonated; }
    public int getTotalReceived() { return totalReceived; }
    public void setTotalReceived(int totalReceived) { this.totalReceived = totalReceived; }
}