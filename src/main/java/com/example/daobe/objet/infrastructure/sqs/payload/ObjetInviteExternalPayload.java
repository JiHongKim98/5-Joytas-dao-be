package com.example.daobe.objet.infrastructure.sqs.payload;

import com.example.daobe.objet.domain.event.ObjetInviteEvent;

public record ObjetInviteExternalPayload(
        String eventType,
        Attributes attributes
) {

    private static final String EVENT_TYPE = "OBJET_INVITE";

    public static ObjetInviteExternalPayload from(ObjetInviteEvent event) {
        return new ObjetInviteExternalPayload(
                EVENT_TYPE,
                Attributes.from(event)
        );
    }

    // Nested
    private record Attributes(
            Long senderId,
            Long receiverId,
            Long domainId,
            String domainName
    ) {

        public static Attributes from(ObjetInviteEvent event) {
            return new Attributes(
                    event.senderId(),
                    event.receiverId(),
                    event.objetId(),
                    event.objectName()
            );
        }
    }
}
