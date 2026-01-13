package com.mts.socialvibe_app.features.notifications.service;

import com.mts.socialvibe_app.features.notifications.dto.NotificationDto;
import com.mts.socialvibe_app.features.notifications.model.Notification;
import com.mts.socialvibe_app.features.notifications.model.NotificationType;
import com.mts.socialvibe_app.features.notifications.repository.NotificationRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService{

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotification(UserEntity recipient, UserEntity actor, NotificationType type, Long targetId) {
        if (recipient.getId().equals(actor.getId())) {
            return;
        }
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setType(type);
        notification.setTargetId(targetId);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getMyNotifications(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return notificationRepository.findAllByRecipientUsernameOrderByCreatedAtDesc(user.getUsername())
                .stream().map(NotificationDto::mapToDto).toList();
    }


    @Override
    public Long getUnreadCount(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return notificationRepository.countByRecipientUsernameAndIsReadFalse(user.getUsername());
    }

    @Override
    @Transactional
    public void makeAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void makeAllAsRead(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<Notification> unread = notificationRepository.findAllByRecipientUsernameAndIsReadFalse(user.getUsername());
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

}