package com.news.payload.request;

import com.news.dto.AbstractDTO;
import com.news.model.User;

import java.util.List;

public class SignupRequest extends AbstractDTO<SignupRequest> {

    private String password;
    private String email;
    private String fullName;
    private List<String> role;

    public SignupRequest() {
        super();
    }

    public SignupRequest(User entity) {
        super();
        this.setId(entity.getId());
        this.password = entity.getPassword();
        this.email = entity.getEmail();
        this.fullName = entity.getFullname();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

}
