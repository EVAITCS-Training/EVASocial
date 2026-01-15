package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.response.NotificationResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.Notification;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.enums.NotificationType;
import com.horrorcore.eva_social.exceptions.ForbiddenException;
import com.horrorcore.eva_social.exceptions.UserNotFoundException;
import com.horrorcore.eva_social.repositories.NotificationRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import com.horrorcore.eva_social.utils.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createNotification(UserCredential recipient, UserCredential actor, NotificationType type,
                                  Post post, Comment comment, String message) {
        // Don't create notification if actor is the same as recipient
        if (recipient.getEmail().equals(actor.getEmail())) {
            return;
        }

        Notification notification = Notification.builder()
                .recipient(recipient)
                .actor(actor)
                .type(type)
                .targetPost(post)
                .targetComment(comment)
                .message(message)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String userEmail) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        
        return notifications.stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUnreadNotifications(String userEmail) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
        
        return notifications.stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(String userEmail, Long notificationId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserNotFoundException("Notification not found"));

        if (!notification.getRecipient().getEmail().equals(user.getEmail())) {
            throw new ForbiddenException("Cannot mark other users' notifications as read");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String userEmail) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
        
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public long getUnreadCount(String userEmail) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        UserProfile actorProfile = userProfileRepository.findByUserCredential(notification.getActor()).orElse(null);
        UserSummaryResponse actorSummary = userMapper.toUserSummary(notification.getActor(), actorProfile);

        return NotificationResponse.builder()
                .id(notification.getId())
                .actor(actorSummary)
                .type(notification.getType())
                .targetPostId(notification.getTargetPost() != null ? notification.getTargetPost().getId() : null)
                .targetCommentId(notification.getTargetComment() != null ? notification.getTargetComment().getId() : null)
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
