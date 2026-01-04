package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostDto;

public interface IPostService {
    PostDto createPost(PostDto postDto, String username);
}
