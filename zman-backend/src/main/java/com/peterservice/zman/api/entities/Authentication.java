package com.peterservice.zman.api.entities;

public class Authentication {
    private String userName;
    private Boolean isAuthenticated;

    public Authentication(String userName, Boolean isAuthenticated) {
        this.userName = userName;
        this.isAuthenticated = isAuthenticated;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getAuthentication() {
        return isAuthenticated;
    }

    public void setAuthentication(Boolean authentication) {
        isAuthenticated = authentication;
    }
}