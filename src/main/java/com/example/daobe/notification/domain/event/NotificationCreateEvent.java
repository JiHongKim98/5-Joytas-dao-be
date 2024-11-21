package com.example.daobe.notification.domain.event;

import com.example.daobe.notification.domain.Notification;

public record NotificationCreateEvent(
        Long notificationId,
        Long receiverId
) {

    public static NotificationCreateEvent of(Notification notification) {
        return new NotificationCreateEvent(
                notification.getId(),
                notification.getReceiverId()
        );
    }
}
