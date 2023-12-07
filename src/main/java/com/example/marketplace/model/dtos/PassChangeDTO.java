package com.example.marketplace.model.dtos;


import com.example.marketplace.model.validation.FieldMatch;

@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords should match")
public class PassChangeDTO {
    private String password;
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public PassChangeDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public PassChangeDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
