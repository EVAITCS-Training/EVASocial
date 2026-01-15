package com.horrorcore.eva_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostResponse {
    private Long id;
    private UserSummaryResponse author;
    private String status;
    private boolean isPublic;
    private boolean isDraft;
    private long viewCount;
    private long likes;
    private long dislikes;
    private long commentCount;
    private boolean likedByCurrentUser;
    private boolean dislikedByCurrentUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> hashtags;
}
