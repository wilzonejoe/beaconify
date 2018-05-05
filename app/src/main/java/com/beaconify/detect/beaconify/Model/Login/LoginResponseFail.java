package com.beaconify.detect.beaconify.Model.Login;

public class LoginResponseFail {
    private int statusCode;
    private String message;

    public LoginResponseFail() {
        this.statusCode = 0;
        this.message = "";
    }

    public LoginResponseFail(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
