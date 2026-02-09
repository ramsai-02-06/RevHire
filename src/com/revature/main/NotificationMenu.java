package com.revature.main;

import com.revature.service.NotificationService;

public class NotificationMenu {

    private static NotificationService service = new NotificationService();

    public static void show(int userId) {
        service.showNotifications(userId);
    }
}
