package com.example.lendahand;

public class UserScore {
    public String name;
    public int score;
    public String avatarUrl;

    public UserScore(String name, int score, String avatarUrl) {
        this.name = name;
        this.score = score;
        this.avatarUrl = avatarUrl;
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public String getAvatarUrl(){
        return avatarUrl;
    }
}