package com.iotait.superpuntos.fcm;

public class Token {

    //this is the model when user log in then one of log in token is created for each user
    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
