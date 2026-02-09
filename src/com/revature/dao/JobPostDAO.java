package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class JobPostDAO {

	
	
    public void createJob(
            int userId,
            String title,
            String description,
            String skills,
            int experience,
            String education,
            String location,
            double salary,
            String jobType) {

        String sql =
            "INSERT INTO job_post " +
            "(employer_id, title, description, skills, experience_required, education, location, salary, job_type) " +
            "VALUES (" +
            "(SELECT employer_id FROM employer WHERE user_id=?), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, skills);
            ps.setInt(5, experience);
            ps.setString(6, education);
            ps.setString(7, location);
            ps.setDouble(8, salary);
            ps.setString(9, jobType);
           

            ps.executeUpdate();
            System.out.println("Job posted successfully");

        } catch (Exception e) {
           // e.printStackTrace();
        	System.out.println("Not able to Create a job.");
        }
        
       

    }

    
    public void viewAllOpenJobs() {

        String sql =
            "SELECT job_id, title, location, salary, job_type " +
            "FROM job_post WHERE status='OPEN'";

        boolean found = false;

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nJob ID | Title | Location | Salary | Type");

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("job_id") + " | " +
                    rs.getString("title") + " | " +
                    rs.getString("location") + " | " +
                    rs.getDouble("salary") + " | " +
                    rs.getString("job_type")
                );
            }

            if (!found) {
                System.out.println("No open jobs available.");
            }

        } catch (Exception e) {
        	System.out.println("No open jobs available.");
        }
    }


    public void viewJobsByEmployer(int userId) {

        String sql =
            "SELECT job_id, title, status " +
            "FROM job_post " +
            "WHERE employer_id = (SELECT employer_id FROM employer WHERE user_id=?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nJob ID | Title | Status");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("job_id") + " | " +
                    rs.getString("title") + " | " +
                    rs.getString("status")
                );
            }

        } catch (Exception e) {
        	System.out.println("Unable to load the available JOb posts.");
        }
    }
}
