package com.mts.socialvibe_app.features.likes.repository;

import com.mts.socialvibe_app.features.likes.model.Like;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(UserEntity user, Post post);

    Long countByPostId(Long postId);
}
