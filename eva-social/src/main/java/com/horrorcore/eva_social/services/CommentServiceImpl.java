package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.CreateCommentRequest;
import com.horrorcore.eva_social.dto.request.UpdateCommentRequest;
import com.horrorcore.eva_social.dto.response.CommentResponse;
import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.enums.NotificationType;
import com.horrorcore.eva_social.exceptions.CommentNotFoundException;
import com.horrorcore.eva_social.exceptions.ForbiddenException;
import com.horrorcore.eva_social.exceptions.PostNotFoundException;
import com.horrorcore.eva_social.exceptions.UserNotFoundException;
import com.horrorcore.eva_social.repositories.CommentRepository;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.utils.CommentMapper;
import com.horrorcore.eva_social.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CommentResponse createComment(String authorEmail, CreateCommentRequest request) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .author(author)
                .post(post)
                .parentComment(parentComment)
                .message(request.getMessage())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Create notification for post author
        if (post.getAuthor() != null) {
            notificationService.createNotification(
                post.getAuthor(),
                author,
                NotificationType.COMMENT,
                post,
                savedComment,
                author.getEmail() + " commented on your post"
            );
        }

        return commentMapper.toCommentResponse(savedComment, author, true);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(String authorEmail, Long commentId, UpdateCommentRequest request) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenException("Cannot update other users' comments");
        }

        comment.setMessage(request.getMessage());
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toCommentResponse(savedComment, author, true);
    }

    @Override
    @Transactional
    public void deleteComment(String authorEmail, Long commentId) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenException("Cannot delete other users' comments");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getPostComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        UserCredential currentUser = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUser() : null;

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        
        // Filter only top-level comments (no parent comment)
        return comments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(comment -> commentMapper.toCommentResponse(comment, currentUser, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentReplies(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        UserCredential currentUser = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUser() : null;

        List<Comment> replies = commentRepository.findByParentCommentOrderByCreatedAtAsc(comment);
        
        return replies.stream()
                .map(reply -> commentMapper.toCommentResponse(reply, currentUser, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void likeComment(String userEmail, Long commentId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        // Remove dislike if exists
        comment.getDislikedBy().remove(user);
        
        // Add like
        comment.getLikedBy().add(user);
        commentRepository.save(comment);

        // Create notification
        if (comment.getAuthor() != null) {
            notificationService.createNotification(
                comment.getAuthor(),
                user,
                NotificationType.LIKE,
                comment.getPost(),
                comment,
                user.getEmail() + " liked your comment"
            );
        }
    }

    @Override
    @Transactional
    public void unlikeComment(String userEmail, Long commentId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.getLikedBy().remove(user);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void dislikeComment(String userEmail, Long commentId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        // Remove like if exists
        comment.getLikedBy().remove(user);
        
        // Add dislike
        comment.getDislikedBy().add(user);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void undislikeComment(String userEmail, Long commentId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.getDislikedBy().remove(user);
        commentRepository.save(comment);
    }
}
