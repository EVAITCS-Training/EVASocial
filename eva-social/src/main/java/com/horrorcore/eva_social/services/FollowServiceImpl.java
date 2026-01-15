package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.entites.Follow;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.exceptions.InvalidRequestException;
import com.horrorcore.eva_social.exceptions.UserNotFoundException;
import com.horrorcore.eva_social.repositories.FollowRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public void followUser(String followerEmail, Long followingId) {
        UserCredential follower = userCredentialRepository.findById(followerEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        
        UserProfile followingProfile = userProfileRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("User to follow not found"));
        
        UserCredential following = followingProfile.getUserCredential();

        if (follower.getEmail().equals(following.getEmail())) {
            throw new InvalidRequestException("Cannot follow yourself");
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new InvalidRequestException("Already following this user");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        
        followRepository.save(follow);

        // Update counts
        UserProfile followerProfile = userProfileRepository.findByUserCredential(follower).orElse(null);
        if (followerProfile != null) {
            followerProfile.setFollowingCount(followerProfile.getFollowingCount() + 1);
            userProfileRepository.save(followerProfile);
        }
        
        followingProfile.setFollowerCount(followingProfile.getFollowerCount() + 1);
        userProfileRepository.save(followingProfile);
    }

    @Override
    @Transactional
    public void unfollowUser(String followerEmail, Long followingId) {
        UserCredential follower = userCredentialRepository.findById(followerEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        
        UserProfile followingProfile = userProfileRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("User to unfollow not found"));
        
        UserCredential following = followingProfile.getUserCredential();

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new InvalidRequestException("Not following this user"));

        followRepository.delete(follow);

        // Update counts
        UserProfile followerProfile = userProfileRepository.findByUserCredential(follower).orElse(null);
        if (followerProfile != null) {
            followerProfile.setFollowingCount(Math.max(0, followerProfile.getFollowingCount() - 1));
            userProfileRepository.save(followerProfile);
        }
        
        followingProfile.setFollowerCount(Math.max(0, followingProfile.getFollowerCount() - 1));
        userProfileRepository.save(followingProfile);
    }

    @Override
    public boolean isFollowing(String followerEmail, Long followingId) {
        UserCredential follower = userCredentialRepository.findById(followerEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        
        UserProfile followingProfile = userProfileRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        UserCredential following = followingProfile.getUserCredential();
        
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Override
    public long getFollowerCount(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        return followRepository.countByFollowing(profile.getUserCredential());
    }

    @Override
    public long getFollowingCount(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        return followRepository.countByFollower(profile.getUserCredential());
    }
}
