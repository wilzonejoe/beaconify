package com.beaconify.detect.beaconify.Model;

public class User
{
    private String userId;
    private String username;
    private String firstname;
    private String lastname;

    public User()
    {
        this.userId = this.username = this.firstname = this.lastname = "";
    }

    public User(String userId, String username, String firstname, String lastname) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
