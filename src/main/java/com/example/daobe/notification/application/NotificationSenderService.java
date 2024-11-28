package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.payload.NotificationInfoPayload;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.notification.exception.NotificationException;
import com.example.daobe.notification.exception.NotificationExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSenderService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public void sendMessage(Long notificationId, Long senderId) {
        Notification notification = notificationRepository.findByIdWithSender(notificationId, senderId)
                .orElseThrow(() -> new NotificationException(NotificationExceptionType.NOT_EXIST_NOTIFICATION));
        List<NotificationEmitter> emitters = emitterRepository.findAllByUserId(notification.getReceiverId());

        NotificationInfoPayload payload = NotificationInfoPayload.from(notification);
        emitters.forEach(emitter -> sendToClient(emitter, payload));
    }

    private void sendToClient(NotificationEmitter emitter, NotificationInfoPayload payload) {
        try {
            emitter.sendToClient(payload);
        } catch (Exception ex) {
            emitterRepository.deleteById(emitter.getEmitterId());
        }
    }
}
