package com.mts.socialvibe_app.features.relationship.repository;

import com.mts.socialvibe_app.features.relationship.model.Relationship;
import com.mts.socialvibe_app.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findByFollowerUsernameAndFollowingId(String followerUsername, Long targetUserId);

    @Query("select count(r) from Relationship r where r.follower.id = :id")
    Long countFollowingByFollowerId(@Param("id") Long id);

    @Query("select count(r) from Relationship r where r.following.id = :id")
    Long countFollowerByFollowingId(@Param("id") Long id);

    @Query("select count(r) > 0 from Relationship r " +
            "where r.follower.id = :targetId and r.following.id = :currentUserId")
    boolean isFollowingBack(@Param("currentUserId") Long currentUserId, @Param("targetId") Long targetId);

    @Query("select r1.following from Relationship r1 " +
            "where r1.follower.id = :userId " +
            "and exists (select 1 from Relationship r2 " +
            "where r2.follower.id = r1.following.id and r2.following.id = :userId)")
    List<UserEntity> findMutualFollows(@Param("userId") Long userId);

    @Query("select r.following from Relationship r " +
            "where r.follower.id = :friendId " +
            "and exists (select 1 from Relationship r2 " +
            "where r2.follower.id = r.following.id and r2.following.id = :friendId)")
    List<UserEntity> findFriendsOfUser(@Param("friendId") Long friendId);
}
