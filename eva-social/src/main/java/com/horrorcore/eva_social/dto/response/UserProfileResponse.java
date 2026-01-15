package com.horrorcore.eva_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Long id;
    private String email;
    private String displayName;
    private String bio;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private String location;
    private String website;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private boolean isPrivate;
    private boolean isVerified;
    private long followerCount;
    private long followingCount;
    private long postCount;
}
