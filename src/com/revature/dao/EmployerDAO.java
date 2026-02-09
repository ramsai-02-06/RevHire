package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class EmployerDAO {

    public boolean employerExists(int userId) {

        String sql = "SELECT employer_id FROM employer WHERE user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            //e.printStackTrace();
        
        	System.out.println("Unable to load the emp Details");
        }
        return false;
    }

    public void createEmployerProfile(
            int userId,
            String companyName,
            String industry,
            String description,
            String location,
            String designation) {

        String sql =
            "INSERT INTO employer (user_id, company_name, industry, description, location, designation) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, companyName);
            ps.setString(3, industry);
            ps.setString(4, description);
            ps.setString(5, location);
            ps.setString(6, designation);

            ps.executeUpdate();
            System.out.println("Employer profile created successfully");

        } catch (Exception e) {
        	System.out.println("Unable to Create the emp Profile");
        }
        
    }
    
    public boolean isProfileCompleted(int userId) {

        String sql =
            "SELECT description FROM employer WHERE user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("description") != null;
            }

        } catch (Exception e) {
        	System.out.println("Can't get the Status");
        }
        return false;
    }

    
    public boolean hasApplications(int userId) {

        String sql =
            "SELECT 1 FROM job_application a " +
            "JOIN job_post j ON a.job_id = j.job_id " +
            "JOIN employer e ON j.employer_id = e.employer_id " +
            "WHERE e.user_id=? LIMIT 1";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeQuery().next();

        } catch (Exception e) {
           // e.printStackTrace();
        	System.out.println("unable to load the applications check once the applications option");
        	
        }
        return false;
    }

    
}
