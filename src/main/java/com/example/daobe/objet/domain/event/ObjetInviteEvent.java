package com.example.daobe.objet.domain.event;

import com.example.daobe.objet.domain.ObjetSharer;
import java.util.UUID;

public record ObjetInviteEvent(
        String eventId,
        Long senderId,
        Long receiverId,
        Long objetId,
        String objectName
) {

    public static ObjetInviteEvent of(Long senderId, ObjetSharer objetSharer) {
        return new ObjetInviteEvent(
                UUID.randomUUID().toString(),
                senderId,
                objetSharer.getId(),
                objetSharer.getObjet().getId(),
                objetSharer.getObjet().getName()
        );
    }
}
