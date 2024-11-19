package com.example.daobe.objet.infrastructure.sqs;

import com.example.daobe.common.sqs.SqsQueuePublisher;
import com.example.daobe.objet.application.ObjetExternalEventPublisher;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.infrastructure.sqs.payload.ObjetInviteExternalPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisObjetExternalEventPublisher implements ObjetExternalEventPublisher {

    private final SqsQueuePublisher sqsQueuePublisher;

    @Override
    public void execute(ObjetInviteEvent event) {
        ObjetInviteExternalPayload payload = ObjetInviteExternalPayload.from(event);
        sqsQueuePublisher.execute(payload);
    }
}
