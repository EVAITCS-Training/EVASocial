package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByTag(String tag);
    List<Hashtag> findTop10ByOrderByUseCountDesc();
}
