package com.example.daobe.notification.application.dto.command;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.user.domain.User;

public record CreateNotificationCommand(
        Long senderId,
        Long receiverId,
        Long domainId,
        String domainName,
        String eventType
) {

    public Notification toNotification(User user) {
        return Notification.builder()
                .senderUser(user)
                .receiverId(receiverId)
                .domainId(domainId)
                .domainName(domainName)
                .type(eventType)
                .build();
    }
}
