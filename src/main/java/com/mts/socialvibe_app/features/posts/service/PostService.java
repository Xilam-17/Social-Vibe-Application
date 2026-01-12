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
public class PostService implements IPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
    }

    @Override
    public PostResponse createPost(PostRequest postRequest, String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Post post = Post.mapToEntity(postRequest);
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return mapToPostResponse(savedPost, username);
    }

    @Override
    public List<PostResponse> getAllPosts(String username) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(post -> this.mapToPostResponse(post, username)).toList();
    }

    @Override
    public List<PostResponse> getMyPosts(String username) {
        List<Post> posts = postRepository.findByUserUsernameIgnoreCaseOrderByCreatedAtDesc(username);
        return posts.stream().map(post -> this.mapToPostResponse(post, username)).toList();
    }

    @Override
    public PostResponse editPost(Long id, String username, PostRequest postRequest) {
        Post post = getPost(id);

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to edit this post!");
        }

        post.setCaption(postRequest.getCaption());
        post.setLocation(postRequest.getLocation());

        if (postRequest.getImageUrl() != null) {
            post.setImageUrl(postRequest.getImageUrl());
        }

        Post updatedPost = postRepository.save(post);
        return this.mapToPostResponse(updatedPost, username);
    }

    @Override
    public void deletePost(Long id, String username) {
        Post post = getPost(id);
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getFeed(String username) {
        List<Post> posts = postRepository.findFeedByUsername(username);
        return posts.stream()
                .map(post -> this.mapToPostResponse(post, username))
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByUsername(String targetUsername, String currentUsername) {
        List<Post> posts = postRepository.findByUserUsernameIgnoreCaseOrderByCreatedAtDesc(targetUsername);
        return posts.stream().map(post -> this.mapToPostResponse(post, currentUsername)).toList();
    }


    public PostResponse mapToPostResponse(Post post, String username) {
        boolean isLiked = likeRepository.findByUserUsernameAndPostId(username, post.getId()).isPresent();
        long likeCount = likeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.mapToDto(post, isLiked, likeCount, commentCount);
    }
}