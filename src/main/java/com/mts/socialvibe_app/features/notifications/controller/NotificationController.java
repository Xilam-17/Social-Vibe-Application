package com.mts.socialvibe_app.features.notifications.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.notifications.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController extends BaseController {

    private final INotificationService service;

    @GetMapping
    public ResponseWrapper getMyNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.NOTIFICATION_RETRIEVE_SUCCESS, service.getMyNotifications(username));
    }

    @GetMapping("/unread-count")
    public ResponseWrapper getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.NOTIFICATION_COUNT_SUCCESS,service.getUnreadCount(username));
    }

    @PatchMapping("/{notifyId}/read")
    public ResponseWrapper makeAsRead(@PathVariable("notifyId") Long notifyId) {
        service.makeAsRead(notifyId);
        return createResponse(MessageCode.NOTIFICATION_RETRIEVE_SUCCESS, "Notification marked as read");
    }

    @PatchMapping("/read-all")
    public ResponseWrapper markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        service.makeAllAsRead(username);
        return createResponse(MessageCode.NOTIFICATION_RETRIEVE_SUCCESS, "All notifications marked as read");
    }
}
