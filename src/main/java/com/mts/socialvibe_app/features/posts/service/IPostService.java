package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postRequest, String username);

    List<PostResponse> getAllPosts(String username);

    List<PostResponse> getMyPosts(String username);

    PostResponse editPost(Long id, String username, PostRequest postRequest);

    void deletePost(Long id, String username);

    List<PostResponse> getFeed(String username);
}
