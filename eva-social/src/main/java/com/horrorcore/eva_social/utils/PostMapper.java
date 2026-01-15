package com.horrorcore.eva_social.utils;

import com.horrorcore.eva_social.dto.response.FeedPostResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.Hashtag;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostMapper {

    private final UserMapper userMapper;
    private final UserProfileRepository userProfileRepository;

    public PostResponse toPostResponse(Post post, UserCredential currentUser) {
        UserProfile authorProfile = post.getAuthor() != null ?
                userProfileRepository.findByUserCredential(post.getAuthor()).orElse(null) : null;

        UserSummaryResponse authorSummary = authorProfile != null ?
                userMapper.toUserSummary(authorProfile) :
                (post.getAuthor() != null ? userMapper.toUserSummary(post.getAuthor(), null) : null);

        boolean likedByCurrentUser = currentUser != null && post.getLikedBy() != null &&
                post.getLikedBy().contains(currentUser);
        boolean dislikedByCurrentUser = currentUser != null && post.getDislikedBy() != null &&
                post.getDislikedBy().contains(currentUser);

        return PostResponse.builder()
                .id(post.getId())
                .author(authorSummary)
                .status(post.getStatus())
                .isPublic(post.isPublic())
                .isDraft(post.isDraft())
                .viewCount(post.getViewCount())
                .likes(post.getLikes())
                .dislikes(post.getDislikes())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .dislikedByCurrentUser(dislikedByCurrentUser)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hashtags(post.getHashtags() != null ?
                        post.getHashtags().stream()
                                .map(Hashtag::getTag)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public FeedPostResponse toFeedPostResponse(Post post, UserCredential currentUser, boolean isFollowing) {
        PostResponse postResponse = toPostResponse(post, currentUser);
        return FeedPostResponse.builder()
                .id(postResponse.getId())
                .author(postResponse.getAuthor())
                .status(postResponse.getStatus())
                .isPublic(postResponse.isPublic())
                .isDraft(postResponse.isDraft())
                .viewCount(postResponse.getViewCount())
                .likes(postResponse.getLikes())
                .dislikes(postResponse.getDislikes())
                .commentCount(postResponse.getCommentCount())
                .likedByCurrentUser(postResponse.isLikedByCurrentUser())
                .dislikedByCurrentUser(postResponse.isDislikedByCurrentUser())
                .createdAt(postResponse.getCreatedAt())
                .updatedAt(postResponse.getUpdatedAt())
                .hashtags(postResponse.getHashtags())
                .isFollowing(isFollowing)
                .build();
    }
}
