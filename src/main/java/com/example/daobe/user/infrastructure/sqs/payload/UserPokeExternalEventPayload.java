package com.example.daobe.user.infrastructure.sqs.payload;

import com.example.daobe.user.domain.event.UserPokeEvent;

public record UserPokeExternalEventPayload(
        String eventType,
        Attributes attributes
) {

    private static final String EVENT_TYPE = "USER_POKE";

    public static UserPokeExternalEventPayload from(UserPokeEvent event) {
        return new UserPokeExternalEventPayload(
                EVENT_TYPE,
                Attributes.from(event)
        );
    }

    // Nested
    private record Attributes(
            String eventId,
            Long senderId,
            Long receiverId
    ) {

        public static Attributes from(UserPokeEvent event) {
            return new Attributes(
                    event.eventId(),
                    event.senderId(),
                    event.receiverId()
            );
        }
    }
}
