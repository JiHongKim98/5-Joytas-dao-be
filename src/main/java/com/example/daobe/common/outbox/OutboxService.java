package com.example.daobe.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class OutboxService {

    private static final String NOT_EXIST_EVENT_ID_LOG = "존재하지 않는 이벤트입니다.";

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public void appendEventWithIdentify(String eventId, OutboxEventType eventType, Object eventClass) {
        OutboxPayload payload = OutboxPayload.of(eventType.name(), eventClass);
        String serializedPayload = serialize(payload);
        Outbox outbox = Outbox.builder()
                .id(eventId)
                .eventType(eventType)
                .payload(serializedPayload)
                .build();
        outboxRepository.save(outbox);
    }

    public void completeEvent(String eventId) {
        Outbox outbox = outboxRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException(NOT_EXIST_EVENT_ID_LOG));
        outbox.complete();
        outboxRepository.save(outbox);
    }

    private String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
