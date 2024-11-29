package com.example.daobe.objet.application;

import com.example.daobe.objet.domain.event.ObjetInviteEvent;

public interface ObjetExternalEventPublisher {

    void execute(ObjetInviteEvent event);
}
