package com.revature.model;

public class User {

    private int userId;
    private String email;
    private String password;
    private String role;
    private String securityQuestion;
    private String securityAnswer;

    public User() {}

    public User(String email, String password, String role,
                String securityQuestion, String securityAnswer) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public User(int userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswer() { return securityAnswer; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}
