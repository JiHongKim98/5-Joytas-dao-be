package com.example.daobe.common.outbox;

import com.example.daobe.common.sqs.SqsQueuePublisher;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private static final int MAX_RELAY_PROCESS = 100;
    private static final String FAIL_TO_REPUBLISH_LOG_FORMAT =
            "Failed to publish outbox relay message\n| >> EVENT_ID: {}\n| >> CURRENT_DELIVERY_COUNT: {}";

    private final OutboxRepository outboxRepository;
    private final SqsQueuePublisher sqsQueuePublisher;
    private final CustomOutboxRepository customOutboxRepository;

    @Transactional
    @Scheduled(fixedRate = 2000)
    @SchedulerLock(name = "outboxRelaySchedulerLock", lockAtLeastFor = "PT2S", lockAtMostFor = "PT4S")
    public void scheduledOutboxRelay() {
        LocalDateTime beforeTwoSeconds = LocalDateTime.now().minusSeconds(2);
        List<Outbox> outboxes = outboxRepository.findAllBy(beforeTwoSeconds, MAX_RELAY_PROCESS);
        if (!outboxes.isEmpty()) {
            for (Outbox outbox : outboxes) {
                outbox.increaseDeliveryCount();
                republishEvent(outbox);
            }
            customOutboxRepository.updateAll(outboxes);
        }
    }

    private void republishEvent(Outbox outbox) {
        try {
            sqsQueuePublisher.execute(outbox.getPayload());
            outbox.complete();
        } catch (Exception e) {
            log.warn(FAIL_TO_REPUBLISH_LOG_FORMAT, outbox.getId(), outbox.getDeliveryCount());
        }
    }
}
