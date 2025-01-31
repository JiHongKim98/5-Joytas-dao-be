package com.example.daobe.common.outbox;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutboxRepository extends JpaRepository<Outbox, String> {

    @Query("""
            SELECT ob FROM Outbox ob
            WHERE ob.createdAt < :time
                AND ob.isComplete = false
                AND ob.deliveryCount < 5
            ORDER BY ob.createdAt
            LIMIT :limit
            """)
    List<Outbox> findAllBy(LocalDateTime time, int limit);
}
