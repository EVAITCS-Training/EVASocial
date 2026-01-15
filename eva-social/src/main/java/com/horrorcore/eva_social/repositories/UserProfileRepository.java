package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserCredential(UserCredential userCredential);
    Optional<UserProfile> findByDisplayNameIgnoreCase(String displayName);
    List<UserProfile> findByDisplayNameContainingIgnoreCase(String query);
}
