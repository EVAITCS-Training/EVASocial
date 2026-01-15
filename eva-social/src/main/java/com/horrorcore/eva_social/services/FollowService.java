package com.horrorcore.eva_social.services;

public interface FollowService {
    void followUser(String followerEmail, Long followingId);
    void unfollowUser(String followerEmail, Long followingId);
    boolean isFollowing(String followerEmail, Long followingId);
    long getFollowerCount(Long userId);
    long getFollowingCount(Long userId);
}
