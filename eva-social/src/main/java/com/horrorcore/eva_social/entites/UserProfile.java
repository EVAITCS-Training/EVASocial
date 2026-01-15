package com.horrorcore.eva_social.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profiles")
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_email", nullable = false, unique = true)
    private UserCredential userCredential;

    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String displayName;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    @Column(length = 500)
    private String bio;

    @Column(length = 500)
    private String profilePictureUrl;

    @Column(length = 500)
    private String coverPhotoUrl;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    @Column(length = 100)
    private String location;

    @Column(length = 500)
    private String website;

    private LocalDate dateOfBirth;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPrivate = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean isVerified = false;

    @Column(nullable = false)
    @Builder.Default
    private long followerCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private long followingCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private long postCount = 0;
}
