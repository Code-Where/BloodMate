package com.asdeveloper.bloodmate.Models;

public class FcmToken {
    private Long id;
    private String token;

    public FcmToken() {
    }

    public FcmToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
