package com.example.daobe.objet.application;

import com.example.daobe.common.outbox.OutboxEventType;
import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ObjetInternalEventRecorder {

    private final OutboxService outboxService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void recodeInternalEvent(ObjetInviteEvent event) {
        outboxService.appendEventWithIdentify(event.eventId(), OutboxEventType.OBJET_INVITE, event);
    }
}
