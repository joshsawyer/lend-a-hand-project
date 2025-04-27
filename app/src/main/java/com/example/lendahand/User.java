package com.example.lendahand;

//Please feel free to alter anything here this is just a basic setup for the user class so I can get the GUI working
public class User {
    private String name;
    private int itemsRequested;
    private int progress; //percentage points for items requested

    //constructor
    public User(String name, int itemsRequested, int progress){
        this.name = name;
        this.itemsRequested = itemsRequested;
        this.progress = progress;
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
}
