package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.UpdateProfileRequest;
import com.horrorcore.eva_social.dto.response.UserProfileResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.entites.Follow;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.exceptions.UserNotFoundException;
import com.horrorcore.eva_social.repositories.FollowRepository;
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
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final FollowRepository followRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponse getProfile(String email) {
        UserCredential user = userCredentialRepository.findById(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        UserProfile profile = userProfileRepository.findByUserCredential(user)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for: " + email));
        
        return userMapper.toUserProfileResponse(profile);
    }

    @Override
    public UserProfileResponse getProfileById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User profile not found with id: " + id));
        
        return userMapper.toUserProfileResponse(profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        UserCredential user = userCredentialRepository.findById(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        UserProfile profile = userProfileRepository.findByUserCredential(user)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for: " + email));

        if (request.getDisplayName() != null) {
            profile.setDisplayName(request.getDisplayName());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        if (request.getCoverPhotoUrl() != null) {
            profile.setCoverPhotoUrl(request.getCoverPhotoUrl());
        }
        if (request.getLocation() != null) {
            profile.setLocation(request.getLocation());
        }
        if (request.getWebsite() != null) {
            profile.setWebsite(request.getWebsite());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getIsPrivate() != null) {
            profile.setPrivate(request.getIsPrivate());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        return userMapper.toUserProfileResponse(savedProfile);
    }

    @Override
    @Transactional
    public UserProfileResponse createProfile(UserCredential userCredential) {
        UserProfile profile = UserProfile.builder()
                .userCredential(userCredential)
                .displayName(userCredential.getEmail().split("@")[0])
                .build();
        
        UserProfile savedProfile = userProfileRepository.save(profile);
        return userMapper.toUserProfileResponse(savedProfile);
    }

    @Override
    public List<UserSummaryResponse> searchUsers(String query) {
        List<UserProfile> profiles = userProfileRepository.findByDisplayNameContainingIgnoreCase(query);
        return profiles.stream()
                .map(userMapper::toUserSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSummaryResponse> getFollowers(String email) {
        UserCredential user = userCredentialRepository.findById(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        List<Follow> follows = followRepository.findByFollowing(user);
        return follows.stream()
                .map(follow -> {
                    UserProfile profile = userProfileRepository.findByUserCredential(follow.getFollower()).orElse(null);
                    return userMapper.toUserSummary(follow.getFollower(), profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSummaryResponse> getFollowing(String email) {
        UserCredential user = userCredentialRepository.findById(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        List<Follow> follows = followRepository.findByFollower(user);
        return follows.stream()
                .map(follow -> {
                    UserProfile profile = userProfileRepository.findByUserCredential(follow.getFollowing()).orElse(null);
                    return userMapper.toUserSummary(follow.getFollowing(), profile);
                })
                .collect(Collectors.toList());
    }
}
