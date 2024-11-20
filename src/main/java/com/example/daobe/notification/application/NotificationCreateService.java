package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.command.CreateNotificationCommand;
import com.example.daobe.notification.application.dto.payload.NotificationExternalMessagePayload;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCreateService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final NotificationExternalEventPublisher externalEventPublisher;

    public void createNotification(CreateNotificationCommand command) {
        Long userId = command.senderId();
        User findUser = userService.getUserById(userId);

        Notification notification = command.toNotification(findUser);
        notificationRepository.save(notification);

        NotificationExternalMessagePayload payload = NotificationExternalMessagePayload.of(notification);
        externalEventPublisher.execute(payload);
    }
}
