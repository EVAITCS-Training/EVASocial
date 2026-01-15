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
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "author_email")
    private UserCredential author;

    @Size(min = 5, max = 300)
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPublic = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDraft = false;

    @Column(nullable = false)
    @Builder.Default
    private long viewCount = 0;

    @ManyToMany
    @JoinTable(
        name = "post_likes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_email")
    )
    @Builder.Default
    private Set<UserCredential> likedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "post_dislikes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_email")
    )
    @Builder.Default
    private Set<UserCredential> dislikedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "post_hashtags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    @Builder.Default
    private Set<Hashtag> hashtags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new java.util.ArrayList<>();

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

    // Maintain backwards compatibility - deprecated
    /**
     * @deprecated Likes are now managed through the likedBy relationship.
     * This method is a no-op maintained for backwards compatibility.
     */
    @Deprecated
    public void setLikes(long likes) {
        // No-op for backwards compatibility with existing code
    }

    /**
     * @deprecated Dislikes are now managed through the dislikedBy relationship.
     * This method is a no-op maintained for backwards compatibility.
     */
    @Deprecated
    public void setDislikes(long dislikes) {
        // No-op for backwards compatibility with existing code
    }
}
