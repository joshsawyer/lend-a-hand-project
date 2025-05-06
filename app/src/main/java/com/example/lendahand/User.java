package com.example.lendahand;

//Please feel free to alter anything here this is just a basic setup for the user class so I can get the GUI working
public class User {
    private String name;
    private int itemsRequested;
    private int progress; //percentage points for items requested
    private int resourceID;
    private int requestID;
    private String resourceName;
    private String requestBio;
    private int amountRequested;
    //constructor
    public User(String name, int itemsRequested, int progress){/*, int resourceID,int requestID, String resourceName, String requestBio){*/
        this.name = name;
        this.itemsRequested = itemsRequested;
        this.progress = progress;
        this.resourceID = resourceID;
        this.requestID = requestID;
        this.resourceName = resourceName;
        this.requestBio = requestBio;

    }

    public String getName(){
        return name;
    }

    public int getItemsRequested(){
        return itemsRequested;
    }

    public int getProgress(){
        return progress;
    }
    public int getResourceID(){return resourceID;}

    public int getRequestID() {return requestID;}

    public String getResourceName() {return resourceName;}

    public String getRequestBio() {return requestBio;}

    public int getAmountRequested() {return 5;}


}
