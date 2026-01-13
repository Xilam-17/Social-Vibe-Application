package com.mts.socialvibe_app.features.posts.repository;

import com.mts.socialvibe_app.features.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserUsernameIgnoreCaseOrderByCreatedAtDesc(String username);

    @Query("select p from Post p where p.user.id in" +
    "(select r.following.id from Relationship r where r.follower.username = :username)" +
    "order by p.createdAt desc")
    List<Post> findFeedByUsername(@Param("username") String username);

}
