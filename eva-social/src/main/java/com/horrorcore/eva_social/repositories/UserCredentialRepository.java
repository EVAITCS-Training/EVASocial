package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
}
