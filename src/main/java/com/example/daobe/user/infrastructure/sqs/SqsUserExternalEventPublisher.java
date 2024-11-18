package com.example.daobe.user.infrastructure.sqs;

import com.example.daobe.common.sqs.SqsQueuePublisher;
import com.example.daobe.user.application.UserExternalEventPublisher;
import com.example.daobe.user.domain.event.UserPokeEvent;
import com.example.daobe.user.infrastructure.sqs.payload.UserPokeExternalEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsUserExternalEventPublisher implements UserExternalEventPublisher {

    private final SqsQueuePublisher sqsQueuePublisher;

    public void execute(UserPokeEvent event) {
        UserPokeExternalEventPayload payload = UserPokeExternalEventPayload.from(event);
        sqsQueuePublisher.execute(payload);
    }
}
