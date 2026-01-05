package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IPostService {
    PostDto createPost(PostDto postDto, String username);

    List<PostDto> getAllPosts();

    List<PostDto> getMyPosts(String username);

    PostDto editPost(Long id, String username, PostDto postDto);

    void deletePost(Long id, String username);
}
