package com.mts.socialvibe_app.features.posts.repository;

import com.mts.socialvibe_app.features.posts.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserUsernameOrderByCreatedAtDesc(String username);
}
