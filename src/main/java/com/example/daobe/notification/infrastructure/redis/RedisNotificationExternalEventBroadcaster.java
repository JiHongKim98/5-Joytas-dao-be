package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationExternalEventPublisher;
import com.example.daobe.notification.domain.event.NotificationCreateEvent;
import com.example.daobe.notification.infrastructure.redis.payload.NotificationCreateEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisNotificationExternalEventPublisher implements NotificationExternalEventPublisher {

    private static final String NOTIFICATION_CHANNEL_TOPIC = "notification:channel";

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void execute(NotificationCreateEvent event) {
        NotificationCreateEventPayload payload = NotificationCreateEventPayload.from(event);
        redisTemplate.convertAndSend(
                NOTIFICATION_CHANNEL_TOPIC,
                serialize(payload)
        );
    }

    private String serialize(NotificationCreateEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
