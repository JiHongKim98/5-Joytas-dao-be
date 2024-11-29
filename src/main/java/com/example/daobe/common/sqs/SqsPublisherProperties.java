package com.example.daobe.common.sqs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.sqs")
public record SqsPublisherProperties(
        String queue,
        String groupId
) {
}
