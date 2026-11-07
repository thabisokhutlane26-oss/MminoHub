package com.mminohub.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationManager {

    private final List<MminoNotification> notifications =
            new ArrayList<>();

    public synchronized boolean addNotification(
            MminoNotification notification
    ) {
        if (notification == null || notification.getId() == null) {
            return false;
        }

        for (MminoNotification existing : notifications) {
            if (existing.getId().equals(notification.getId())) {
                return false;
            }
        }

        notifications.add(0, notification);
        return true;
    }

    public synchronized List<MminoNotification> getNotifications() {
        return Collections.unmodifiableList(
                new ArrayList<>(notifications)
        );
    }

    public synchronized List<MminoNotification> getUnreadNotifications() {
        List<MminoNotification> unread = new ArrayList<>();

        for (MminoNotification notification : notifications) {
            if (!notification.isRead()) {
                unread.add(notification);
            }
        }

        return unread;
    }

    public synchronized void markAsRead(String notificationId) {
        for (MminoNotification notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.markAsRead();
                return;
            }
        }
    }

    public synchronized void markAllAsRead() {
        for (MminoNotification notification : notifications) {
            notification.markAsRead();
        }
    }

    public synchronized int getUnreadCount() {
        int count = 0;

        for (MminoNotification notification : notifications) {
            if (!notification.isRead()) {
                count++;
            }
        }

        return count;
    }

    public synchronized boolean removeNotification(String notificationId) {
        if (notificationId == null) return false;

        return notifications.removeIf(
                notification -> notificationId.equals(notification.getId())
        );
    }

    public synchronized void clearNotifications() {
        notifications.clear();
    }
}