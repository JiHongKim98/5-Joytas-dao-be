package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationSenderService;
import com.example.daobe.notification.infrastructure.redis.payload.NotificationCreateEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jihongkim98.redisextensions.annotation.RedisBroadcastListener;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationBroadcastListener {

    private final ObjectMapper objectMapper;
    private final NotificationSenderService notificationSenderService;

    @RedisBroadcastListener(channels = "channel:notification")
    public void notificationBroadcastListener(String message) {
        NotificationCreateEventPayload payload = deserialize(message);
        notificationSenderService.sendMessage(payload.notificationId(), payload.senderId());
    }

    private NotificationCreateEventPayload deserialize(String message) {
        try {
            return objectMapper.readValue(message, NotificationCreateEventPayload.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
