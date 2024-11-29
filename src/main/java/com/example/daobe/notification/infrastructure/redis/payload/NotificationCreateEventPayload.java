package com.example.daobe.notification.infrastructure.redis.payload;

import com.example.daobe.notification.domain.event.NotificationCreateEvent;

public record NotificationCreateEventPayload(
        Long notificationId,
        Long senderId
) {

    public static NotificationCreateEventPayload from(NotificationCreateEvent event) {
        return new NotificationCreateEventPayload(
                event.notificationId(),
                event.senderId()
        );
    }
}
