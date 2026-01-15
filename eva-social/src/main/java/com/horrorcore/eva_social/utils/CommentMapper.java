package com.horrorcore.eva_social.utils;

import com.horrorcore.eva_social.dto.response.CommentResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final UserProfileRepository userProfileRepository;

    public CommentResponse toCommentResponse(Comment comment, UserCredential currentUser) {
        return toCommentResponse(comment, currentUser, false);
    }

    public CommentResponse toCommentResponse(Comment comment, UserCredential currentUser, boolean includeReplies) {
        UserProfile authorProfile = comment.getAuthor() != null ?
                userProfileRepository.findByUserCredential(comment.getAuthor()).orElse(null) : null;

        UserSummaryResponse authorSummary = authorProfile != null ?
                userMapper.toUserSummary(authorProfile) :
                (comment.getAuthor() != null ? userMapper.toUserSummary(comment.getAuthor(), null) : null);

        boolean likedByCurrentUser = currentUser != null && comment.getLikedBy() != null &&
                comment.getLikedBy().contains(currentUser);
        boolean dislikedByCurrentUser = currentUser != null && comment.getDislikedBy() != null &&
                comment.getDislikedBy().contains(currentUser);

        List<CommentResponse> replies = new ArrayList<>();
        if (includeReplies && comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            replies = comment.getReplies().stream()
                    .map(reply -> toCommentResponse(reply, currentUser, false))
                    .collect(Collectors.toList());
        }

        return CommentResponse.builder()
                .id(comment.getId())
                .author(authorSummary)
                .message(comment.getMessage())
                .likes(comment.getLikes())
                .dislikes(comment.getDislikes())
                .likedByCurrentUser(likedByCurrentUser)
                .dislikedByCurrentUser(dislikedByCurrentUser)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(replies)
                .build();
    }
}
