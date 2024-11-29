package com.example.daobe.user.application;

import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.user.domain.event.UserInquiriesEvent;
import com.example.daobe.user.domain.event.UserPokeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class UserExternalEventHandler {

    private final OutboxService outboxService;
    private final UserExternalEventAlert userExternalEventAlert;
    private final UserExternalEventPublisher userExternalEventPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUserPokeEvent(UserPokeEvent event) {
        userExternalEventPublisher.execute(event);
        outboxService.completeEvent(event.eventId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishInquiriesEvent(UserInquiriesEvent event) {
        userExternalEventAlert.execute(event);
    }
}
