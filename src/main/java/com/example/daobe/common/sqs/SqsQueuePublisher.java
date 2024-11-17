package com.example.daobe.common.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsQueuePublisher {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;
    private final SqsPublisherProperties sqsPublisherProperties;

    public SendResult<String> execute(Object payload) {
        String serializedPayload = serialize(payload);
        return execute(serializedPayload);
    }

    public SendResult<String> execute(String payload) {
        return sqsTemplate.send(to -> to
                .queue(sqsPublisherProperties.queue())
                .messageGroupId(sqsPublisherProperties.groupId())
                .payload(payload)
        );
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
