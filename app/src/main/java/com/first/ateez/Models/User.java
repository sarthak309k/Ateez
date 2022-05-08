package com.first.ateez.Models;

public class User {
    private String uid,name,username,phoneNumber,emailid,password,imageProfile;

    public  User()
    {

    }
    public User(String uid, String name, String username, String phoneNumber, String emailid, String password,String imageProfile) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.emailid = emailid;
        this.password = password;
        this.imageProfile=imageProfile;
    }

    public User(String imageProfile) {
        this.imageProfile=imageProfile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
