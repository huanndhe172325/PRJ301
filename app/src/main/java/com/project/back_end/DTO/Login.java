package com.project.back_end.DTO;

public class Login {

    // 1. Email field for login
    private String email;

    // 2. Password field for login
    private String password;

    // 3. Default constructor (implicit, but included here for clarity)
    public Login() {
    }

    // 4. Getters and Setters

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
