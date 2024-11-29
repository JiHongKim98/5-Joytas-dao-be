package com.example.daobe.common.outbox;

import java.util.List;

public interface CustomOutboxRepository {

    void updateAll(List<Outbox> outboxes);
}
