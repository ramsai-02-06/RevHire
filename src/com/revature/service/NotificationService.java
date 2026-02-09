package com.revature.service;

import com.revature.dao.NotificationDAO;

public class NotificationService {

    private NotificationDAO dao = new NotificationDAO();

    public void notifyUser(int userId, String message) {
        dao.createNotification(userId, message);
    }

    public void showNotifications(int userId) {
        dao.viewNotifications(userId);
        dao.markAllAsRead(userId);
    }
}
