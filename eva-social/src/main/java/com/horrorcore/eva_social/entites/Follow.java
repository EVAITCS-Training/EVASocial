package com.horrorcore.eva_social.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follows", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"follower_email", "following_email"})
})
@EntityListeners(AuditingEntityListener.class)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_email", nullable = false)
    private UserCredential follower;

    @ManyToOne
    @JoinColumn(name = "following_email", nullable = false)
    private UserCredential following;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
