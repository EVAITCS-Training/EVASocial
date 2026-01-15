package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.UpdateProfileRequest;
import com.horrorcore.eva_social.dto.response.UserProfileResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.UserCredential;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse getProfile(String email);
    UserProfileResponse getProfileById(Long id);
    UserProfileResponse updateProfile(String email, UpdateProfileRequest request);
    UserProfileResponse createProfile(UserCredential userCredential);
    List<UserSummaryResponse> searchUsers(String query);
    List<UserSummaryResponse> getFollowers(String email);
    List<UserSummaryResponse> getFollowing(String email);
}
