package com.example.daobe.common.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "outboxes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

    @Id
    @Column(name = "outbox_id")
    private String id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private OutboxEventType eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "delivery_count")
    private int deliveryCount;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Builder
    public Outbox(String id, OutboxEventType eventType, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.deliveryCount = 1;
        this.isComplete = false;
        this.createdAt = LocalDateTime.now();
    }

    public void increaseDeliveryCount() {
        deliveryCount++;
    }

    public void complete() {
        isComplete = true;
        publishedAt = LocalDateTime.now();
    }
}
