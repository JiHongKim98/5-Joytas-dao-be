package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationExternalEventBroadcaster;
import com.example.daobe.notification.domain.event.NotificationCreateEvent;
import com.example.daobe.notification.infrastructure.redis.payload.NotificationCreateEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationExternalEventBroadcaster implements NotificationExternalEventBroadcaster {

    private static final String NOTIFICATION_CHANNEL_TOPIC = "channel:notification";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void execute(NotificationCreateEvent event) {
        NotificationCreateEventPayload payload = NotificationCreateEventPayload.from(event);
        redisTemplate.convertAndSend(NOTIFICATION_CHANNEL_TOPIC, payload);
    }
}
