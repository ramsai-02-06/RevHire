package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.model.User;
import com.revature.util.DBConnectionUtil;

public class UserDAO {

    public boolean register(User user) {

        String sql =
            "INSERT INTO users (email, password, role, security_question, security_answer) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getSecurityQuestion());
            ps.setString(5, user.getSecurityAnswer());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {

            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println(
                    "Email already exists. Please login or use Forgot Password."
                );
            } else {
                System.out.println("Registration failed due to database error.");
            }

            return false;
        }

    }

    public User login(String email, String password) {

        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("role")
                );
            }

        } catch (Exception e) {
            
        }
        return null;
    }

    public String getSecurityQuestion(String email) {

        String sql = "SELECT security_question FROM users WHERE email=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("security_question");
            }

        } catch (Exception e) {
        	System.out.println("Cant't able to get the Security question.");
        }
        return null;
    }

    public boolean validateSecurityAnswer(String email, String answer) {

        String sql =
            "SELECT user_id FROM users WHERE email=? AND security_answer=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, answer);

            return ps.executeQuery().next();

        } catch (Exception e) {
        	
          
        }
        return false;
    }

    public void updatePassword(String email, String newPassword) {

        String sql = "UPDATE users SET password=? WHERE email=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
           // e.printStackTrace();
        	
            System.out.println("Can't able to update Password.");
        }
    }
}
