package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.dto.request.UpdateProfileRequest;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.dto.response.UserProfileResponse;
import com.horrorcore.eva_social.dto.response.UserSummaryResponse;
import com.horrorcore.eva_social.services.FollowService;
import com.horrorcore.eva_social.services.PostService;
import com.horrorcore.eva_social.services.UserProfileService;
import com.horrorcore.eva_social.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final FollowService followService;
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfileResponse profile = userProfileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        String email = SecurityUtils.getCurrentUserEmail();
        UserProfileResponse profile = userProfileService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();
        UserProfileResponse profile = userProfileService.updateProfile(email, request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSummaryResponse>> searchUsers(@RequestParam String q) {
        List<UserSummaryResponse> users = userProfileService.searchUsers(q);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable Long id) {
        String currentUserEmail = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUserEmail() : null;
        List<PostResponse> posts = postService.getUserPosts(id, currentUserEmail);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserSummaryResponse>> getUserFollowers(@PathVariable Long id) {
        UserProfileResponse profile = userProfileService.getProfileById(id);
        List<UserSummaryResponse> followers = userProfileService.getFollowers(profile.getEmail());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserSummaryResponse>> getUserFollowing(@PathVariable Long id) {
        UserProfileResponse profile = userProfileService.getProfileById(id);
        List<UserSummaryResponse> following = userProfileService.getFollowing(profile.getEmail());
        return ResponseEntity.ok(following);
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long id) {
        String followerEmail = SecurityUtils.getCurrentUserEmail();
        followService.followUser(followerEmail, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        String followerEmail = SecurityUtils.getCurrentUserEmail();
        followService.unfollowUser(followerEmail, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/isFollowing")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long id) {
        String followerEmail = SecurityUtils.getCurrentUserEmail();
        boolean isFollowing = followService.isFollowing(followerEmail, id);
        return ResponseEntity.ok(isFollowing);
    }
}
