package com.example.shirtsalesapp.activity.auth;

public class RegisterRequest {
    private String UserName;
    private String Password;

    public RegisterRequest(String userName, String password) {
        this.UserName = userName;
        this.Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
