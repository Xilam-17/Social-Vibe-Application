package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postRequest, MultipartFile file, String username) throws IOException;

    List<PostResponse> getAllPosts(String username);

    List<PostResponse> getMyPosts(String username);

    PostResponse editPost(Long id, String username, PostRequest postRequest, MultipartFile file);

    void deletePost(Long id, String username);

    List<PostResponse> getPostsByUsername(String targetUsername, String currentUsername);

    Page<PostResponse> getFollowingFeed(Long id, String username, int page, int size);}
