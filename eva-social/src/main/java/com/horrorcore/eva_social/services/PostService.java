package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.CreatePostRequest;
import com.horrorcore.eva_social.dto.request.UpdatePostRequest;
import com.horrorcore.eva_social.dto.response.FeedPostResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(String authorEmail, CreatePostRequest request);
    PostResponse updatePost(String authorEmail, Long postId, UpdatePostRequest request);
    void deletePost(String authorEmail, Long postId);
    PostResponse getPostById(Long postId, String currentUserEmail);
    List<PostResponse> getUserPosts(Long userId, String currentUserEmail);
    List<FeedPostResponse> getFeed(String userEmail, int page, int size);
    List<PostResponse> getPublicPosts(int page, int size);
    void likePost(String userEmail, Long postId);
    void unlikePost(String userEmail, Long postId);
    void dislikePost(String userEmail, Long postId);
    void undislikePost(String userEmail, Long postId);
    void incrementViewCount(Long postId);
    List<PostResponse> searchPosts(String query);
    List<PostResponse> getPostsByHashtag(String tag);
}
