package com.example.daobe.notification.domain.repository;

import com.example.daobe.notification.domain.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
            SELECT n
            FROM Notification n
            JOIN FETCH n.senderUser u
            WHERE n.id = :id
                AND u.id = :senderId
            """)
    Optional<Notification> findByIdWithSender(Long id, Long senderId);
}
