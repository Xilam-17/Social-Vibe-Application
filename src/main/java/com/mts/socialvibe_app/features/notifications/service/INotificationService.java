package com.mts.socialvibe_app.features.notifications.service;

import com.mts.socialvibe_app.features.notifications.dto.NotificationDto;

import java.util.List;

public interface INotificationService {
    List<NotificationDto> getMyNotifications(String username);

    Long getUnreadCount(String username);

    void makeAsRead(Long id);

    void makeAllAsRead(String username);
}
