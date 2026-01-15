package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.common.LocalStorageService;
import com.mts.socialvibe_app.features.comments.repository.CommentRepository;
import com.mts.socialvibe_app.features.likes.repository.LikeRepository;
import com.mts.socialvibe_app.features.notifications.model.NotificationType;
import com.mts.socialvibe_app.features.notifications.service.NotificationService;
import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
// --- FIXED IMPORTS ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
// ---------------------
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements IPostService {

    @Value("${app.base-url}")
    private String baseUrl;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final LocalStorageService localStorageService;

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
    }

    @Override
    public PostResponse createPost(PostRequest postRequest, MultipartFile file, String username) throws IOException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        String imageURL = localStorageService.saveFile(file);

        Post post = Post.mapToEntity(postRequest);
        post.setImageUrl(imageURL);
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        notificationService.createNotification(post.getUser(), user, NotificationType.POST, savedPost.getId());

        return mapToPostResponse(savedPost, username);
    }

    @Override
    public PostResponse editPost(Long id, String username, PostRequest postRequest, MultipartFile file) {
        Post post = getPost(id);
        if(!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to edit this post");
        }

        post.setCaption(postRequest.getCaption());
        post.setLocation(postRequest.getLocation());

        if(file != null && !file.isEmpty()) {
            try {
                if(post.getImageUrl() != null) {
                    localStorageService.deleteFile(post.getImageUrl());
                }
                String newImageURL = localStorageService.saveFile(file);
                post.setImageUrl(newImageURL);
            } catch (IOException e) {
                throw new RuntimeException("Local file can't be update : " + e.getMessage());
            }
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
        if (post.getImageUrl() != null) {
            localStorageService.deleteFile(post.getImageUrl());
        }
        postRepository.delete(post);
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
    public List<PostResponse> getPostsByUsername(String targetUsername, String currentUsername) {
        List<Post> posts = postRepository.findByUserUsernameIgnoreCaseOrderByCreatedAtDesc(targetUsername);
        return posts.stream().map(post -> this.mapToPostResponse(post, currentUsername)).toList();
    }

    @Override
    public Page<PostResponse> getFollowingFeed(Long userId, String currentUsername, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Post> postPage = postRepository.findFollowingFeed(userId, pageable);

        return postPage.map(post -> mapToPostResponse(post, currentUsername));
    }

    public PostResponse mapToPostResponse(Post post, String username) {
        boolean isLiked = likeRepository.findByUserUsernameAndPostId(username, post.getId()).isPresent();
        long likeCount = likeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.mapToDto(post, isLiked, likeCount, commentCount, baseUrl);
    }
}