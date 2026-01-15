package com.horrorcore.eva_social.controllers;

import com.horrorcore.eva_social.dto.request.CreateCommentRequest;
import com.horrorcore.eva_social.dto.request.UpdateCommentRequest;
import com.horrorcore.eva_social.dto.response.CommentResponse;
import com.horrorcore.eva_social.services.CommentService;
import com.horrorcore.eva_social.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getPostComments(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<CommentResponse>> getCommentReplies(@PathVariable Long id) {
        List<CommentResponse> replies = commentService.getCommentReplies(id);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CreateCommentRequest request) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        CommentResponse comment = commentService.createComment(authorEmail, request);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        CommentResponse comment = commentService.updateComment(authorEmail, id, request);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        String authorEmail = SecurityUtils.getCurrentUserEmail();
        commentService.deleteComment(authorEmail, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        commentService.likeComment(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        commentService.unlikeComment(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        commentService.dislikeComment(userEmail, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/dislike")
    public ResponseEntity<Void> undislikeComment(@PathVariable Long id) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        commentService.undislikeComment(userEmail, id);
        return ResponseEntity.ok().build();
    }
}
