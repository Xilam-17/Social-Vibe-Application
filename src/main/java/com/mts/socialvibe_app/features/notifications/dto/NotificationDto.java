package com.mts.socialvibe_app.features.notifications.dto;

import com.mts.socialvibe_app.features.notifications.model.Notification;
import com.mts.socialvibe_app.features.notifications.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {
    private Long id;
    private String actorUsername;
    private String actorAvatar;
    private NotificationType type;
    private Long targetId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationDto mapToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .actorUsername(notification.getActor().getUsername())
                .actorAvatar(notification.getActor().getAvatarUrl())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

}
