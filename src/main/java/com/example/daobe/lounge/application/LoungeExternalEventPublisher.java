package com.example.daobe.lounge.application;

import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;

public interface LoungeExternalEventPublisher {

    void execute(LoungeInvitedEvent event);
}
