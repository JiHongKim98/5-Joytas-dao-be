package com.example.daobe.notification.application;

import com.example.daobe.notification.domain.event.NotificationCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationExternalEventHandler {

    private final NotificationExternalEventBroadcaster notificationExternalEventBroadCaster;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishNotificationCreateEvent(NotificationCreateEvent event) {
        notificationExternalEventBroadCaster.execute(event);
    }
}
