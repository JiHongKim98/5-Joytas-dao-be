package com.example.daobe.notification.infrastructure.sqs.payload;

import com.example.daobe.notification.application.dto.command.CreateNotificationCommand;

public record NotificationExternalEventPayload(
        String eventType,
        Attributes attributes
) {

    public CreateNotificationCommand toCommand() {
        return new CreateNotificationCommand(
                this.attributes.senderId(),
                this.attributes.receiverId(),
                this.attributes.domainId(),
                this.attributes.domainName(),
                this.eventType
        );
    }

    // Nested
    public record Attributes(
            Long senderId,
            Long receiverId,
            Long domainId,
            String domainName
    ) {
    }
}
