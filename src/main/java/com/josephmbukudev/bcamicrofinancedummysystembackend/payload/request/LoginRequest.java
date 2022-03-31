package com.josephmbukudev.bcamicrofinancedummysystembackend.payload.request;

import javax.validation.constraints.NotBlank;

// User Login DTO
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

//    Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
