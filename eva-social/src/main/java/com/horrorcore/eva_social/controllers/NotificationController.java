package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.dto.response.NotificationResponse;
import com.horrorcore.eva_social.services.NotificationService;
import com.horrorcore.eva_social.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        List<NotificationResponse> notifications = notificationService.getUserNotifications(userEmail);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(userEmail);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        notificationService.markAsRead(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        notificationService.markAllAsRead(userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUnreadCount() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        long count = notificationService.getUnreadCount(userEmail);
        return ResponseEntity.ok(count);
    }
}
