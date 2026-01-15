package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Notification;
import com.horrorcore.eva_social.entites.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(UserCredential recipient);
    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(UserCredential recipient);
    long countByRecipientAndIsReadFalse(UserCredential recipient);
}
