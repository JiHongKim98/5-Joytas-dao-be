package com.example.daobe.notification.application;

import com.example.daobe.notification.domain.event.NotificationCreateEvent;

public interface NotificationExternalEventBroadcaster {

    void execute(NotificationCreateEvent event);
}
