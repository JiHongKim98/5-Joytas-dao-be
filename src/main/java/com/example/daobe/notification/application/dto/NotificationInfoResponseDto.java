package com.example.daobe.notification.application.dto;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationDomainInfo;
import com.example.daobe.user.domain.User;

public record NotificationInfoResponseDto(
        Long notificationId,
        String type,
        boolean isRead,
        String dateTime,
        DomainInfo detail,
        SenderUserInfo sender
) {

    public static NotificationInfoResponseDto of(Notification notification) {
        return new NotificationInfoResponseDto(
                notification.getId(),
                notification.getType().type(),
                notification.isRead(),
                notification.getCreatedAt().toString(),
                notification.getDomainInfo() == null ? null : DomainInfo.of(notification.getDomainInfo()),
                SenderUserInfo.of(notification.getSenderUser())
        );
    }

    // Nested
    private record DomainInfo(
            Long domainId,
            String name
    ) {

        public static DomainInfo of(NotificationDomainInfo domainInfo) {
            return new DomainInfo(
                    domainInfo.getDomainId(),
                    domainInfo.getDomainName()
            );
        }
    }

    // Nested
    private record SenderUserInfo(
            Long userId,
            String nickname
    ) {

        public static SenderUserInfo of(User senderUser) {
            return new SenderUserInfo(
                    senderUser.getId(),
                    senderUser.getNickname()
            );
        }
    }
}
