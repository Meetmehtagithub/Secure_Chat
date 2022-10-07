package com.example.chat;


public class Messages  {

    public String message;
    public String senderId;
    public long timestamp;
    public String currenttime;

    public void setMessage(String message) {
        this.message = message;
    }

    public Messages(String message, String senderId, long timestamp, String currenttime) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
    }

    public Messages() {
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }
}
