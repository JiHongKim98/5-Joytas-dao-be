-- V6__update_notification_and_outbox.sql

-- 1. 컬럼명 수정
ALTER TABLE notifications
    RENAME COLUMN receive_user_id TO receiver_id;

ALTER TABLE notifications
    RENAME COLUMN send_user_id TO sender_user;

ALTER TABLE notifications
    RENAME COLUMN type TO notification_type;

ALTER TABLE outboxes
    RENAME COLUMN noti_outbox_id TO outbox_id;

ALTER TABLE outboxes
    RENAME COLUMN aggregate_type TO event_type;

-- 2. 컬럼 추가
ALTER TABLE notifications
    ADD COLUMN domain_name VARCHAR(255);

ALTER TABLE outboxes
    ADD COLUMN delivery_count TINYINT;

ALTER TABLE outboxes
    ADD COLUMN published_at DATETIME(6);
