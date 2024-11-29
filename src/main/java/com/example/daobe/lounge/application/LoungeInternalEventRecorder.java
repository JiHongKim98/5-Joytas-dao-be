package com.example.daobe.lounge.application;

import com.example.daobe.common.outbox.OutboxEventType;
import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LoungeInternalEventRecorder {

    private final OutboxService outboxService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void recordLoungeInvitedEvent(LoungeInvitedEvent event) {
        outboxService.appendEventWithIdentify(event.eventId(), OutboxEventType.LOUNGE_INVITE, event);
    }
}
