package com.example.daobe.common.outbox;

public record OutboxPayload(
        String eventType,
        Object attributes
) {

    public static OutboxPayload of(String eventType, Object attributes) {
        return new OutboxPayload(eventType, attributes);
    }
}
