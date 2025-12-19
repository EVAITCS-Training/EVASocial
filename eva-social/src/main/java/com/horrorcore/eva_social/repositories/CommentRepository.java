package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
