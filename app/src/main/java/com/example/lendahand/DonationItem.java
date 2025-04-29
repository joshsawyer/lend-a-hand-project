package com.example.lendahand;
public class DonationItem {
    private String itemName;
    private int requested;
    private int current;

    public DonationItem(String itemName, int requested, int current) {
        this.itemName = itemName;
        this.requested = requested;
        this.current = current;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRequested() {
        return requested;
    }

    public int getCurrent() {
        return current;
    }
}