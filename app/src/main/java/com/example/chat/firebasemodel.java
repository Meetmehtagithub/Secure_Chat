package com.example.chat;

public class firebasemodel {

    public String name;
    public String image;
    public String uid;
    public String status;
    public String token;


    public void setToken(String token) {
        this.token = token;
    }

    public firebasemodel(String name, String image, String uid, String status, String token) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.status = status;
        this.token=token;
    }

    public firebasemodel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }
}
