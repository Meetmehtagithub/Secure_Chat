package com.example.chat;

public class userProfile {
    public String userName,userID;

    public userProfile() {
    }

    public userProfile(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }
}

