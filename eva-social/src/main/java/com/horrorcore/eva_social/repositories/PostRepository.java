package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorOrderByCreatedAtDesc(UserCredential author);
    List<Post> findByIsPublicTrueOrderByCreatedAtDesc();
    List<Post> findByAuthorInAndIsPublicTrueOrderByCreatedAtDesc(List<UserCredential> authors);
    Optional<Post> findByIdAndAuthor(Long id, UserCredential author);
    List<Post> findByStatusContainingIgnoreCaseAndIsPublicTrue(String keyword);

    @Query("SELECT p FROM Post p JOIN p.hashtags h WHERE h.tag = :tag AND p.isPublic = true ORDER BY p.createdAt DESC")
    List<Post> findByHashtagOrderByCreatedAtDesc(@Param("tag") String tag);
}

