package com.example.daobe.notification.application;

import com.example.daobe.notification.domain.event.NotificationCreateEvent;

public interface NotificationExternalEventPublisher {

    void execute(NotificationCreateEvent event);
}
