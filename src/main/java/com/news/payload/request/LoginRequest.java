package com.news.payload.request;

import com.news.model.User;

public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
