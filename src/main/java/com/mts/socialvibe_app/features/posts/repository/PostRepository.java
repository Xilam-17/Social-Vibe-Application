package com.mts.socialvibe_app.features.posts.repository;

import com.mts.socialvibe_app.features.posts.model.Post;
// RIGHT IMPORTS
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserUsernameIgnoreCaseOrderByCreatedAtDesc(String username);

    Long countByUserId(Long id);

    @Query("select p from Post p where p.user.id in " +
            "(select r.following.id from Relationship r where r.follower.id = :userId) " +
            "or p.user.id = :userId")
    Page<Post> findFollowingFeed(@Param("userId") Long userId, Pageable pageable);
}