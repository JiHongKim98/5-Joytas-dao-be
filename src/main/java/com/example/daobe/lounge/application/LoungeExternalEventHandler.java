package com.example.daobe.lounge.application;

import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LoungeExternalEventHandler {

    private final OutboxService outboxService;
    private final LoungeExternalEventPublisher loungeExternalEventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishLoungeInvitedEvent(LoungeInvitedEvent event) {
        loungeExternalEventPublisher.execute(event);
        outboxService.completeEvent(event.eventId());
    }
}
