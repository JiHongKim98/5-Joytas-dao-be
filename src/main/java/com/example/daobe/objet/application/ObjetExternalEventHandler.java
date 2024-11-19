package com.example.daobe.objet.application;

import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ObjetExternalEventHandler {

    private final OutboxService outboxService;
    private final ObjetExternalEventPublisher objetExternalEventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void publishExternalEvent(ObjetInviteEvent event) {
        objetExternalEventPublisher.execute(event);
        outboxService.completeEvent(event.eventId());
    }
}
