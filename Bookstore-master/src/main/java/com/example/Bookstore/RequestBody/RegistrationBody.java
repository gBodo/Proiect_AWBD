package com.example.Bookstore.RequestBody;

import jakarta.validation.constraints.*;

public class RegistrationBody {
    @NotBlank
    @Size(min = 3 , max = 255)
    private String username;
    @NotBlank
    @Size(min = 8 , max = 100)
    private String password;
    @NotBlank
    @Email
    private String email;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}