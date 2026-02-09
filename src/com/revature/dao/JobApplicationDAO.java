package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class JobApplicationDAO {

    public void viewApplicationsForEmployer(int userId) {

        String sql =
            "SELECT a.application_id, a.status, a.applied_date " +
            "FROM job_application a " +
            "JOIN job_post j ON a.job_id=j.job_id " +
            "WHERE j.employer_id = (SELECT employer_id FROM employer WHERE user_id=?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nApp ID | Status | Applied Date");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("application_id") + " | " +
                    rs.getString("status") + " | " +
                    rs.getTimestamp("applied_date")
                );
            }

        } catch (Exception e) {
           // e.printStackTrace();
        	System.out.println("Unable to load the available Applications.");
        }
    }

    public void applyJob(int userId, int jobId) {

        String checkSql =
            "SELECT status FROM job_application " +
            "WHERE job_id=? AND seeker_id=" +
            "(SELECT seeker_id FROM job_seeker WHERE user_id=?)";

        String insertSql =
            "INSERT INTO job_application (job_id, seeker_id, resume_id) " +
            "VALUES (?, " +
            "(SELECT seeker_id FROM job_seeker WHERE user_id=?), " +
            "(SELECT resume_id FROM resume WHERE seeker_id=" +
            "(SELECT seeker_id FROM job_seeker WHERE user_id=?)))";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setInt(1, jobId);
            checkPs.setInt(2, userId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");

                if ("WITHDRAWN".equals(status)) {
                    // allow reapply → delete old record
                    String deleteSql =
                        "DELETE FROM job_application " +
                        "WHERE job_id=? AND seeker_id=" +
                        "(SELECT seeker_id FROM job_seeker WHERE user_id=?)";

                    try (PreparedStatement delPs =
                             con.prepareStatement(deleteSql)) {
                        delPs.setInt(1, jobId);
                        delPs.setInt(2, userId);
                        delPs.executeUpdate();
                    }
                } else {
                    System.out.println(
                        "You have already applied for this job (Status: " + status + ")"
                    );
                    return;
                }
            }

            try (PreparedStatement insertPs =
                     con.prepareStatement(insertSql)) {

                insertPs.setInt(1, jobId);
                insertPs.setInt(2, userId);
                insertPs.setInt(3, userId);
                insertPs.executeUpdate();

                System.out.println("Applied successfully.");
            }

        } catch (Exception e) {
           // e.printStackTrace();
            System.out.println("Not able to Apply.");
        }
    }

    public void viewMyApplications(int userId) {

        String sql =
            "SELECT a.application_id, j.title, a.status, a.applied_date " +
            "FROM job_application a " +
            "JOIN job_post j ON a.job_id=j.job_id " +
            "WHERE a.seeker_id = (SELECT seeker_id FROM job_seeker WHERE user_id=?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nApp ID | Job | Status | Date");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("application_id") + " | " +
                    rs.getString("title") + " | " +
                    rs.getString("status") + " | " +
                    rs.getTimestamp("applied_date")
                );
            }

        } catch (Exception e) {
        	System.out.println("Unable to see the Applications");
        }
    }

    public boolean withdrawApplication(
            int applicationId, int userId, String reason) {

        String sql =
            "UPDATE job_application SET status='WITHDRAWN', withdrawn_reason=? " +
            "WHERE application_id=? " +
            "AND status='APPLIED' " +
            "AND seeker_id = (SELECT seeker_id FROM job_seeker WHERE user_id=?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reason);
            ps.setInt(2, applicationId);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
        	System.out.println("Withdraw is not possible Right now. Try after Sometime!!");
        }
        return false;
    }


    
    public void updateApplicationStatus(int applicationId, String status, String comment) {

        String sql =
            "UPDATE job_application SET status=?, employer_comment=? WHERE application_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, applicationId);
            ps.executeUpdate();

        } catch (Exception e) {
        	System.out.println("Status is Unavailable");
        }
    }

}
