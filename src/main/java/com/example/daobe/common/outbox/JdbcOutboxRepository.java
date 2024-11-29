package com.example.daobe.common.outbox;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOutboxRepository implements CustomOutboxRepository {

    private static final String BULK_UPDATE_QUERY = """
            UPDATE outboxes
                SET is_complete = ?, published_at = ?, delivery_count = ?
            WHERE outbox_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Override
    public void updateAll(List<Outbox> outboxes) {
        jdbcTemplate.batchUpdate(BULK_UPDATE_QUERY, outboxes, outboxes.size(), (ps, outbox) -> {
            ps.setBoolean(1, true);
            ps.setObject(2, outbox.getPublishedAt());
            ps.setInt(3, outbox.getDeliveryCount());
            ps.setString(4, outbox.getId());
        });
        entityManager.clear();
    }
}
