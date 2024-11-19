package com.example.daobe.lounge.infrastructure.sqs;

import com.example.daobe.common.sqs.SqsQueuePublisher;
import com.example.daobe.lounge.application.LoungeExternalEventPublisher;
import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;
import com.example.daobe.lounge.infrastructure.sqs.payload.LoungeInviteExternalPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsLoungeExternalEventPublisher implements LoungeExternalEventPublisher {

    private final SqsQueuePublisher sqsQueuePublisher;

    @Override
    public void execute(LoungeInvitedEvent event) {
        LoungeInviteExternalPayload payload = LoungeInviteExternalPayload.from(event);
        sqsQueuePublisher.execute(payload);
    }
}
