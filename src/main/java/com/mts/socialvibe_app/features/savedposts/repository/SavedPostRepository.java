package com.mts.socialvibe_app.features.savedposts.repository;

import com.mts.socialvibe_app.features.savedposts.model.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {

    Optional<SavedPost> findByUserUsernameAndPostId(String username, Long postId);

    Long countByUserId(Long userId);

    List<SavedPost> findByUserIdOrderByCreatedAtDesc(Long userId);
}
