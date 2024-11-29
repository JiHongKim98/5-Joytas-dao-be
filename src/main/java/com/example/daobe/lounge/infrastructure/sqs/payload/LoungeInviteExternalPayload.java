package com.example.daobe.lounge.infrastructure.sqs.payload;

import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;

public record LoungeInviteExternalPayload(
        String eventType,
        Attributes attributes
) {

    private static final String EVENT_TYPE = "LOUNGE_INVITE";

    public static LoungeInviteExternalPayload from(LoungeInvitedEvent event) {
        return new LoungeInviteExternalPayload(
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

        public static Attributes from(LoungeInvitedEvent event) {
            return new Attributes(
                    event.senderId(),
                    event.receiverId(),
                    event.loungeId(),
                    event.loungeName()
            );
        }
    }
}
