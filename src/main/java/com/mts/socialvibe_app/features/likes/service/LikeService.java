package com.mts.socialvibe_app.features.likes.service;

import com.mts.socialvibe_app.features.likes.dto.LikeDto;
import com.mts.socialvibe_app.features.likes.model.Like;
import com.mts.socialvibe_app.features.likes.repository.LikeRepository;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService{

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LikeDto toggleLike(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Post not found"));

        UserEntity user = userRepository.findByUsername(username);

        if(user == null) throw new RuntimeException("User not found");

        Optional<Like> existingLike = likeRepository.findByUserUsernameAndPost(user.getUsername(), post);
        boolean isLiked;
        String message;

        if(existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            likeRepository.flush();
            isLiked = false;
            message = "Post unliked";
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            likeRepository.flush();
            isLiked = true;
            message = "Post liked";
        }

        Long countLikes = likeRepository.countByPostId(postId);
        return new LikeDto(countLikes, isLiked, message);
    }
}
