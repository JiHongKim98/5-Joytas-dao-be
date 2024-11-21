package com.example.daobe.notification.infrastructure.redis.payload;

import com.example.daobe.notification.domain.event.NotificationCreateEvent;

public record NotificationCreateEventPayload(
        Long receiverId,
        Long notificationId
) {

    public static NotificationCreateEventPayload from(NotificationCreateEvent event) {
        return new NotificationCreateEventPayload(
                event.receiverId(),
                event.notificationId()
        );
    }
}
