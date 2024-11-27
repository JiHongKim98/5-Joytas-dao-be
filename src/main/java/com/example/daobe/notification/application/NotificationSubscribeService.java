package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.DummyResponseDto;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.notification.exception.NotificationException;
import com.example.daobe.notification.exception.NotificationExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationSubscribeService {

    // TODO: SSE 구독 및 알림 발행 분리

    private static final Long DEFAULT_TIMEOUT = 44 * 1000L;  // 기본 지속 시간 44초

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribeNotification(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        NotificationEmitter notificationEmitter = NotificationEmitter.builder()
                .userId(userId)
                .emitter(emitter)
                .build();
        emitterRepository.save(notificationEmitter);

        configurationEmitter(emitter, notificationEmitter.getEmitterId());
        sendToClient(notificationEmitter, DummyResponseDto.of());

        return emitter;
    }

    public void publishToClient(Long notificationId, Long senderId) {
        Notification notification = notificationRepository.findByIdWithSender(notificationId, senderId)
                .orElseThrow(() -> new NotificationException(NotificationExceptionType.NOT_EXIST_NOTIFICATION));
        List<NotificationEmitter> emitterList = emitterRepository.findAllByUserId(notification.getReceiverId());
        NotificationInfoResponseDto response = NotificationInfoResponseDto.of(notification);
        emitterList.forEach(emitter -> sendToClient(emitter, response));
    }

    private void configurationEmitter(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
        });
        emitter.onError((error) -> {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
        });
    }

    private <T> void sendToClient(NotificationEmitter emitter, T data) {
        try {
            emitter.sendToClient(data);
        } catch (Exception ex) {
            emitterRepository.deleteById(emitter.getEmitterId());
        }
    }
}
