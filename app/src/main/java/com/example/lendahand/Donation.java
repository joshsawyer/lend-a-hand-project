package com.example.lendahand;

import java.sql.Timestamp;

public class Donation {
    private int donationID;
    private String userID;
    private int resourceID;
    private int amountDonated;
    private Timestamp dateDonated;

    public Donation(int donationID, String userID, int resourceID, int amountDonated, Timestamp dateDonated) {
        this.donationID = donationID;
        this.userID = userID;
        this.resourceID = resourceID;
        this.amountDonated = amountDonated;
        this.dateDonated = dateDonated;
    }
    public int getDonationID() { return donationID; }
    public void setDonationID(int donationID) { this.donationID = donationID; }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public int getResourceID() { return resourceID; }
    public void setResourceID(int resourceID) { this.resourceID = resourceID; }
    public int getAmountDonated() { return amountDonated; }
    public void setAmountDonated(int amountDonated) { this.amountDonated = amountDonated; }
    public Timestamp getDateDonated() { return dateDonated; }
    public void setDateDonated(Timestamp dateDonated) { this.dateDonated = dateDonated; }
}
