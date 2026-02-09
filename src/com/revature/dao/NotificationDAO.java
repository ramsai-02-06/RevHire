package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.util.DBConnectionUtil;

public class NotificationDAO {

    public void createNotification(int userId, String message) {

        String sql =
            "INSERT INTO notification (user_id, message) VALUES (?, ?)";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Can't Create the Notification.");
        }
    }

    public void viewNotifications(int userId) {

        String sql =
            "SELECT notification_id, message, created_at " +
            "FROM notification WHERE user_id=? ORDER BY created_at DESC";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Notifications ---");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("notification_id") + " | " +
                    rs.getString("message") + " | " +
                    rs.getTimestamp("created_at")
                );
            }

        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Notification is Not Available.");
        	
        }
    }

    public void markAllAsRead(int userId) {

        String sql = "UPDATE notification SET is_read=1 WHERE user_id=?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
           // e.printStackTrace();
        }
    }
}
