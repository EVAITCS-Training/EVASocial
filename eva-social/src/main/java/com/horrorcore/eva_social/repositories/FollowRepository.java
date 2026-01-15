package com.horrorcore.eva_social.repositories;

import com.horrorcore.eva_social.entites.Follow;
import com.horrorcore.eva_social.entites.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(UserCredential follower, UserCredential following);
    List<Follow> findByFollower(UserCredential follower);
    List<Follow> findByFollowing(UserCredential following);
    boolean existsByFollowerAndFollowing(UserCredential follower, UserCredential following);
    long countByFollowing(UserCredential following);
    long countByFollower(UserCredential follower);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower = :follower AND f.following = :following")
    void unfollowUser(@Param("follower") UserCredential follower, @Param("following") UserCredential following);
}
