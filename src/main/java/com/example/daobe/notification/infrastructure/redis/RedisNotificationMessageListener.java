package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationSubscribeService;
import com.example.daobe.notification.infrastructure.redis.payload.NotificationCreateEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationSubscribeService notificationSubscribeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        NotificationCreateEventPayload payload = deserializeFromBytes(message.getBody());
        notificationSubscribeService.publishToClient(payload.notificationId(), payload.senderId());
    }

    private NotificationCreateEventPayload deserializeFromBytes(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, NotificationCreateEventPayload.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
