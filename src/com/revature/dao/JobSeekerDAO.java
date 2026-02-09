package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class JobSeekerDAO {

    public boolean jobSeekerExists(int userId) {

        String sql = "SELECT seeker_id FROM job_seeker WHERE user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            //e.printStackTrace();
            
        }
        return false;
    }

    public void createJobSeekerProfile(
            int userId,
            String fullName,
            String phone,
            String location,
            int experienceYears) {

        String sql =
            "INSERT INTO job_seeker (user_id, full_name, phone, location, experience_years) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, phone);
            ps.setString(4, location);
            ps.setInt(5, experienceYears);

            ps.executeUpdate();
            System.out.println("Job seeker profile created");

        } catch (Exception e) {
           // e.printStackTrace();
            System.out.println("Job seeker profile is not created");
        }
    }
    
    public boolean profileExists(int userId) {

        String sql =
            "SELECT 1 FROM job_seeker WHERE user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            System.out.println("Error checking job seeker profile");
        }
        return false;
    }
    
    public void createProfile(
            int userId,
            String fullName,
            String phone,
            String location,
            int experienceYears) {

        String sql =
            "INSERT INTO job_seeker (user_id, full_name, phone, location, experience_years) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, phone);
            ps.setString(4, location);
            ps.setInt(5, experienceYears);

            ps.executeUpdate();

        } catch (Exception e) {
        	System.out.println("Error creating job seeker profile");
        }
    }
}
