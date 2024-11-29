package com.example.daobe.user.domain.event;

import java.util.UUID;

public record UserPokeEvent(
        String eventId,
        Long senderId,
        Long receiverId
) {

    public static UserPokeEvent of(Long senderId, Long userId) {
        return new UserPokeEvent(
                UUID.randomUUID().toString(),
                senderId,
                userId
        );
    }
}
