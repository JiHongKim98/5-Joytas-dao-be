package com.example.daobe.notification.domain;

import static com.example.daobe.notification.exception.NotificationExceptionType.IS_NOT_OWN_NOTIFICATION;

import com.example.daobe.common.domain.BaseTimeEntity;
import com.example.daobe.notification.exception.NotificationException;
import com.example.daobe.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @Column(name = "noti_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_id")
    private Long receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user")
    private User senderUser;

    @Embedded
    private NotificationDomainInfo domainInfo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationEventType type;

    @Column(name = "is_read")
    private boolean isRead;

    @Builder
    public Notification(User senderUser, Long receiverId, Long domainId, String domainName, String type) {
        this.senderUser = senderUser;
        this.receiverId = receiverId;
        this.domainInfo = domainId == null ? null : new NotificationDomainInfo(domainId, domainName);
        this.type = NotificationEventType.convertByStringEventType(type);
        this.isRead = false;
    }

    public void updateReadStateIfOwnNotification(Long userId) {
        if (!Objects.equals(receiverId, userId)) {
            throw new NotificationException(IS_NOT_OWN_NOTIFICATION);
        }
        isRead = true;
    }
}
