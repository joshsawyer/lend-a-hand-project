package com.example.lendahand;

public class RequestItem {
    private String itemName;
    private int requested;
    private int received;
    private String requestBio;
    private String dateRequested;


    public RequestItem(String itemName, int requested, int received, String requestBio, String dateRequested) {
        this.itemName = itemName;
        this.requested = requested;
        this.received = received;
        this.requestBio = requestBio;
        this.dateRequested = dateRequested;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRequested() {
        return requested;
    }

    public int getReceived() {
        return received;
    }

    public String getRequestBio() {
        return requestBio;
    }

    public String getDateRequested(){return dateRequested;}

}
