package com.example.daobe.notification.application.dto.payload;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationDomainInfo;
import com.example.daobe.user.domain.User;

public record NotificationInfoPayload(
        Long notificationId,
        String type,
        DomainInfo detail,
        SenderUserInfo sender
) {

    public static NotificationInfoPayload from(Notification notification) {
        return new NotificationInfoPayload(
                notification.getId(),
                notification.getType().type(),
                notification.getDomainInfo() == null ? null : DomainInfo.of(notification.getDomainInfo()),
                SenderUserInfo.of(notification.getSenderUser())
        );
    }

    // Nested
    private record DomainInfo(
            String name
    ) {

        public static DomainInfo of(NotificationDomainInfo domainInfo) {
            return new DomainInfo(
                    domainInfo.getDomainName()
            );
        }
    }

    // Nested
    private record SenderUserInfo(
            String nickname
    ) {

        public static SenderUserInfo of(User senderUser) {
            return new SenderUserInfo(
                    senderUser.getNickname()
            );
        }
    }
}
