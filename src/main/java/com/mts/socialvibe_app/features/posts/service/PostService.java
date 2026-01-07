package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.comments.repository.CommentRepository;
import com.mts.socialvibe_app.features.likes.repository.LikeRepository;
import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements IPostService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;


    private Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
        return post;
    }

    @Override
    public PostResponse createPost(PostRequest postRequest, String username) {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new RuntimeException("User not found ");
        }
        Post post = Post.mapToEntity(postRequest);
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return PostResponse.mapToDto(savedPost);
    }

    @Override
    public List<PostResponse> getAllPosts(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(PostResponse::mapToDto).toList();
    }


    @Override
    public List<PostResponse> getMyPosts(String username) {
        List<Post> posts = postRepository.findByUserUsernameOrderByCreatedAtDesc(username);
        return posts.stream().map(PostResponse::mapToDto).toList();
    }

    @Override
    public PostResponse editPost(Long id, String username, PostRequest postRequest) {
        Post post = getPost(id);

        if(!post.getUser().getUsername().equals(username)){
            throw new RuntimeException("You are not authorized to edit this post!");
        }

        post.setCaption(postRequest.getCaption());
        post.setLocation(postRequest.getLocation());
        Post updatedPost = postRepository.save(post);

        return PostResponse.mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id, String username) {
        Post post = getPost(id);
        if(!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getFeed(String username) {
        return List.of();
    }


}
