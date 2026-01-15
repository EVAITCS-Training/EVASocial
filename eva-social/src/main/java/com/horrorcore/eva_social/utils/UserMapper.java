package com.horrorcore.eva_social.utils;

import com.horrorcore.eva_social.dto.response.UserProfileResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSummaryResponse toUserSummary(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        return UserSummaryResponse.builder()
                .email(profile.getUserCredential().getEmail())
                .displayName(profile.getDisplayName())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .isVerified(profile.isVerified())
                .build();
    }

    public UserSummaryResponse toUserSummary(UserCredential user, UserProfile profile) {
        if (profile == null) {
            return UserSummaryResponse.builder()
                    .email(user.getEmail())
                    .displayName(user.getEmail())
                    .profilePictureUrl(null)
                    .isVerified(false)
                    .build();
        }
        return toUserSummary(profile);
    }

    public UserProfileResponse toUserProfileResponse(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        return UserProfileResponse.builder()
                .id(profile.getId())
                .email(profile.getUserCredential().getEmail())
                .displayName(profile.getDisplayName())
                .bio(profile.getBio())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .coverPhotoUrl(profile.getCoverPhotoUrl())
                .location(profile.getLocation())
                .website(profile.getWebsite())
                .dateOfBirth(profile.getDateOfBirth())
                .createdAt(profile.getCreatedAt())
                .isPrivate(profile.isPrivate())
                .isVerified(profile.isVerified())
                .followerCount(profile.getFollowerCount())
                .followingCount(profile.getFollowingCount())
                .postCount(profile.getPostCount())
                .build();
    }
}
