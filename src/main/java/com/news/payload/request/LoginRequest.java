package com.news.payload.request;

import com.news.model.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
