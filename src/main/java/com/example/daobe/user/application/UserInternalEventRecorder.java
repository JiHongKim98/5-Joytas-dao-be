package com.example.daobe.user.application;

import com.example.daobe.common.outbox.OutboxEventType;
import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.user.domain.event.UserPokeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserInternalEventRecorder {

    private final OutboxService outboxService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void recordUserPokeEvent(UserPokeEvent event) {
        outboxService.appendEventWithIdentify(event.eventId(), OutboxEventType.USER_POKE, event);
    }
}
