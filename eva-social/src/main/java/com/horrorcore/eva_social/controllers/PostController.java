package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.dto.request.CreatePostRequest;
import com.horrorcore.eva_social.dto.request.UpdatePostRequest;
import com.horrorcore.eva_social.dto.response.FeedPostResponse;
import com.horrorcore.eva_social.dto.response.HashtagResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.services.HashtagService;
import com.horrorcore.eva_social.services.PostService;
import com.horrorcore.eva_social.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final HashtagService hashtagService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPublicPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<PostResponse> posts = postService.getPublicPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<FeedPostResponse>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        List<FeedPostResponse> feed = postService.getFeed(userEmail, page, size);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        String currentUserEmail = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUserEmail() : null;
        PostResponse post = postService.getPostById(id, currentUserEmail);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        PostResponse post = postService.createPost(authorEmail, request);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        PostResponse post = postService.updatePost(authorEmail, id, request);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        postService.deletePost(authorEmail, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        postService.likePost(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        postService.unlikePost(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikePost(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        postService.dislikePost(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/dislike")
    public ResponseEntity<Void> undislikePost(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        postService.undislikePost(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String q) {
        List<PostResponse> posts = postService.searchPosts(q);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/hashtag/{tag}")
    public ResponseEntity<List<PostResponse>> getPostsByHashtag(@PathVariable String tag) {
        List<PostResponse> posts = postService.getPostsByHashtag(tag);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/trending-hashtags")
    public ResponseEntity<List<HashtagResponse>> getTrendingHashtags() {
        List<HashtagResponse> hashtags = hashtagService.getTrendingHashtags();
        return ResponseEntity.ok(hashtags);
    }
}
