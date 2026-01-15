package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.response.NotificationResponse;
import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    void createNotification(UserCredential recipient, UserCredential actor, NotificationType type, 
                          Post post, Comment comment, String message);
    List<NotificationResponse> getUserNotifications(String userEmail);
    List<NotificationResponse> getUnreadNotifications(String userEmail);
    void markAsRead(String userEmail, Long notificationId);
    void markAllAsRead(String userEmail);
    long getUnreadCount(String userEmail);
}
