package com.example.daobe.notification.domain;

import static com.example.daobe.notification.exception.NotificationExceptionType.NON_MATCH_DOMAIN_EVENT_TYPE;

import com.example.daobe.notification.exception.NotificationException;
import java.util.stream.Stream;

public enum NotificationEventType {
    LOUNGE_INVITE("N0001"),
    OBJET_INVITE("N0002"),
    USER_POKE("N0003"),
    ;

    private final String type;

    NotificationEventType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static NotificationEventType convertByStringEventType(String eventType) {
        return Stream.of(NotificationEventType.values())
                .filter(value -> value.name().equals(eventType))
                .findFirst()
                .orElseThrow(() -> new NotificationException(NON_MATCH_DOMAIN_EVENT_TYPE));
    }
}
