package com.example.lendahand;

public class UserScore {
    public String name;
    public int score;

    public UserScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }
}