package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Comment;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
    List<Comment> findByParentCommentOrderByCreatedAtAsc(Comment parentComment);
    List<Comment> findByAuthorOrderByCreatedAtDesc(UserCredential author);
}

