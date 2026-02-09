package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class ResumeDAO {

	public boolean resumeExists(int userId) {

        String sql =
            "SELECT 1 FROM resume r " +
            "JOIN job_seeker js ON r.seeker_id = js.seeker_id " +
            "WHERE js.user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeQuery().next();

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    public void createOrUpdateResume(
            int userId,
            String objective,
            String education,
            int experience,
            String skills) {

        if (resumeExists(userId)) {
            updateResume(userId, objective, education, experience, skills);
        } else {
            createResume(userId, objective, education, experience, skills);
        }
    }

    private void createResume(
            int userId,
            String objective,
            String education,
            int experience,
            String skills) {

        String sql =
            "INSERT INTO resume (seeker_id, objective, education, experience, skills) " +
            "VALUES ( " +
            "(SELECT seeker_id FROM job_seeker WHERE user_id=?), ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, objective);
            ps.setString(3, education);
            ps.setInt(4, experience);
            ps.setString(5, skills);

            ps.executeUpdate();
            System.out.println("Resume created successfully");

        } catch (Exception e) {
//            e.printStackTrace();
        	System.out.println("Can't Create Resume Right Now . Please Try Again");
        }
    }

    private void updateResume(
            int userId,
            String objective,
            String education,
            int experience,
            String skills) {

        String sql =
            "UPDATE resume SET objective=?, education=?, experience=?, skills=? " +
            "WHERE seeker_id = (SELECT seeker_id FROM job_seeker WHERE user_id=?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, objective);
            ps.setString(2, education);
            ps.setInt(3, experience);
            ps.setString(4, skills);
            ps.setInt(5, userId);

            ps.executeUpdate();
            System.out.println("Resume updated successfully");

        } catch (Exception e) {
//            e.printStackTrace();
        	System.out.println("Can't Update the Resume");
        }
    }
    
    public void viewResume(int userId) {

        String sql =
            "SELECT r.objective, r.education, r.experience, r.skills " +
            "FROM resume r " +
            "JOIN job_seeker js ON r.seeker_id=js.seeker_id " +
            "WHERE js.user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Your Resume ---");
                System.out.println("Objective  : " + rs.getString("objective"));
                System.out.println("Education  : " + rs.getString("education"));
                System.out.println("Experience : " + rs.getInt("experience"));
                System.out.println("Skills     : " + rs.getString("skills"));
            }

        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Can't view Resume Right Now.");
        }
    }

    public void saveOrUpdateResume(
            int userId,
            String objective,
            String education,
            int experience,
            String skills) {

        String sql =
            "INSERT INTO resume (seeker_id, objective, education, experience, skills) " +
            "VALUES ( " +
            "(SELECT seeker_id FROM job_seeker WHERE user_id=?), ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "objective=?, education=?, experience=?, skills=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, objective);
            ps.setString(3, education);
            ps.setInt(4, experience);
            ps.setString(5, skills);

            ps.setString(6, objective);
            ps.setString(7, education);
            ps.setInt(8, experience);
            ps.setString(9, skills);

            ps.executeUpdate();

        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Can't Update Resume Right Now.");
        }
    }

    
}
