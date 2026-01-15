package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.CreateCommentRequest;
import com.horrorcore.eva_social.dto.request.UpdateCommentRequest;
import com.horrorcore.eva_social.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(String authorEmail, CreateCommentRequest request);
    CommentResponse updateComment(String authorEmail, Long commentId, UpdateCommentRequest request);
    void deleteComment(String authorEmail, Long commentId);
    List<CommentResponse> getPostComments(Long postId);
    List<CommentResponse> getCommentReplies(Long commentId);
    void likeComment(String userEmail, Long commentId);
    void unlikeComment(String userEmail, Long commentId);
    void dislikeComment(String userEmail, Long commentId);
    void undislikeComment(String userEmail, Long commentId);
}
