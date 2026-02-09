package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public boolean register(User user) {
        return userDAO.register(user);
    }

    public User login(String email, String password) {
        return userDAO.login(email, password);
    }

    public String getSecurityQuestion(String email) {
        return userDAO.getSecurityQuestion(email);
    }

    public boolean validateSecurityAnswer(String email, String answer) {
        return userDAO.validateSecurityAnswer(email, answer);
    }

    public void updatePassword(String email, String newPassword) {
        userDAO.updatePassword(email, newPassword);
    }
}
