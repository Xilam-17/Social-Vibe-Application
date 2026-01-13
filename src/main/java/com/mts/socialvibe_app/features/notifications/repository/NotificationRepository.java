package com.mts.socialvibe_app.features.notifications.repository;

import com.mts.socialvibe_app.features.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByRecipientIdAndIsReadFalse(Long recipientId);

    Long countByRecipientIdAndIsReadFalse(Long recipientId);}
