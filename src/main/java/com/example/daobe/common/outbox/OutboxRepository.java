package com.example.daobe.common.outbox;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutboxRepository extends JpaRepository<Outbox, String> {

    @Query("""
            SELECT o FROM Outbox o
            WHERE o.createdAt < :time
                AND o.isComplete = false
                AND o.deliveryCount < 5
            ORDER BY o.createdAt ASC
            LIMIT 100
            """)
    List<Outbox> findAllBy(LocalDateTime time);
}
