package com.example.daobe.lounge.domain.event;

import com.example.daobe.lounge.domain.Lounge;
import java.util.UUID;

public record LoungeInvitedEvent(
        String eventId,
        Long senderId,
        Long receiverId,
        Long loungeId,
        String loungeName
) {

    public static LoungeInvitedEvent of(Long senderId, Long receiverId, Lounge lounge) {
        return new LoungeInvitedEvent(
                UUID.randomUUID().toString(),
                senderId,
                receiverId,
                lounge.getId(),
                lounge.getName()
        );
    }
}
