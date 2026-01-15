package com.horrorcore.eva_social.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "author_email")
    private UserCredential author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> replies = new java.util.ArrayList<>();

    @Size(min = 3, max = 300)
    @Column(nullable = false)
    private String message;

    @ManyToMany
    @JoinTable(
        name = "comment_likes",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "user_email")
    )
    @Builder.Default
    private Set<UserCredential> likedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "comment_dislikes",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "user_email")
    )
    @Builder.Default
    private Set<UserCredential> dislikedBy = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Convenience methods for like counts
    public long getLikes() {
        return likedBy != null ? likedBy.size() : 0;
    }

    public long getDislikes() {
        return dislikedBy != null ? dislikedBy.size() : 0;
    }

    // Maintain backwards compatibility
    public void setLikes(long likes) {
        // No-op for backwards compatibility
    }

    public void setDislikes(long dislikes) {
        // No-op for backwards compatibility
    }

    // Keep for backwards compatibility with existing code
    public List<Comment> getComments() {
        return replies;
    }

    public void setComments(List<Comment> comments) {
        this.replies = comments;
    }
}
