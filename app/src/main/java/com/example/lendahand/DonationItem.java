package com.example.lendahand;
public class DonationItem {
    private String itemName;
    private int amount_donated;
    private String user_received;

    private String date_donated;

    public DonationItem(String itemName, int amount, String received, String date_donated) {
        this.itemName = itemName;
        this.amount_donated = amount;
        this.user_received = received;
        this.date_donated = date_donated;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmountDonated() {
        return amount_donated;
    }

    public String getUserReceived() {
        return user_received;
    }

    public String getDateDonated() {
        return date_donated;
    }
}