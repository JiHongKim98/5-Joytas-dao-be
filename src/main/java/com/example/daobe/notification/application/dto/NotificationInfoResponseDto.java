package com.example.daobe.notification.application.dto;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationDomainInfo;
import com.example.daobe.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationInfoResponseDto(
        @JsonProperty("notification_id") Long notificationId,
        @JsonProperty("type") String type,
        @JsonProperty("is_read") boolean isRead,
        @JsonProperty("datetime") String dateTime,
        @JsonProperty("detail") DomainInfo detail,
        @JsonProperty("sender") SenderUserInfo sender
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
            @JsonProperty("domain_id") Long domainId,
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
            @JsonProperty("user_id") Long userId,
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
