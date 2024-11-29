package com.example.daobe.common.config;

import com.example.daobe.common.decorator.MdcDecorator;
import io.awspring.cloud.autoconfigure.core.CredentialsProperties;
import io.awspring.cloud.autoconfigure.sqs.SqsProperties;
import io.awspring.cloud.sqs.MessageExecutionThreadFactory;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {

    private static final String SQS_LISTENER_THREAD_NAME_PREFIX = "sqs-listener-";

    private final SqsProperties sqsProperties;
    private final CredentialsProperties credentialsProperties;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(awsCredentialsProvider())
                .endpointOverride(sqsProperties.getEndpoint())
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .configure(options -> options
                        .acknowledgementMode(AcknowledgementMode.MANUAL)
                        .componentsTaskExecutor(sqsTaskExecutor())
                )
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @Bean
    public SimpleAsyncTaskExecutor sqsTaskExecutor() {
        MessageExecutionThreadFactory factory = new MessageExecutionThreadFactory();
        factory.setThreadNamePrefix(SQS_LISTENER_THREAD_NAME_PREFIX);
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadFactory(factory);
        executor.setTaskDecorator(new MdcDecorator());
        return executor;
    }

    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    private AwsCredentialsProvider awsCredentialsProvider() {
        AwsBasicCredentials credentials = AwsBasicCredentials
                .create(credentialsProperties.getAccessKey(), credentialsProperties.getSecretKey());
        return StaticCredentialsProvider.create(credentials);
    }
}
