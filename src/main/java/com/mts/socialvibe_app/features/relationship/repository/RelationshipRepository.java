package com.mts.socialvibe_app.features.relationship.repository;

import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.relationship.model.Relationship;
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
    boolean isFollowingBack(@Param("currentUserId") Long currentUserId, @Param("targetId") Long targetId);}
