package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// CrudRepository, ListCrudRepository, PagingAndPaginationRepository, JpaRepository
public interface PostRepository extends JpaRepository<Post, Long> {
}
