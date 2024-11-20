package com.example.daobe.notification.infrastructure.sqs;

import com.example.daobe.notification.application.NotificationCreateService;
import com.example.daobe.notification.infrastructure.sqs.payload.NotificationExternalEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsNotificationListener {

    private final ObjectMapper objectMapper;
    private final NotificationCreateService notificationCreateService;

    @SqsListener(value = "${aws.sqs.joytas.queue}", id = "{sqs.joytas.group-id}")
    public void handleCreateNotification(String payload, Acknowledgement acknowledgement) {
        NotificationExternalEventPayload eventPayload = deserialize(payload);
        notificationCreateService.createNotification(eventPayload.toCommand());
        acknowledgement.acknowledge();
    }

    private NotificationExternalEventPayload deserialize(String value) {
        try {
            return objectMapper.readValue(value, NotificationExternalEventPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
