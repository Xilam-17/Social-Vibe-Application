package com.mts.socialvibe_app.features.likes.repository;

import com.mts.socialvibe_app.features.likes.model.Like;
import com.mts.socialvibe_app.features.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserUsernameAndPost(String username, Post post);

    Long countByPostId(Long postId);
}
