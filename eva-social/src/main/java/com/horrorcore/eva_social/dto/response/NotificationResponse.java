package com.horrorcore.eva_social.dto.response;

import com.horrorcore.eva_social.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private UserSummaryResponse actor;
    private NotificationType type;
    private Long targetPostId;
    private Long targetCommentId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
