package com.peterservice.zman.api.entities;

public class Authentication {
    private String userName;
    private Boolean isAuthentication;

    public Authentication(String userName, Boolean isAuthentication) {
        this.userName = userName;
        this.isAuthentication = isAuthentication;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getAuthentication() {
        return isAuthentication;
    }

    public void setAuthentication(Boolean authentication) {
        isAuthentication = authentication;
    }
}
