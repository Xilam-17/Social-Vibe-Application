package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostDto;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService implements IPostService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    private Post mapToEntity(PostDto dto) {
        Post post = new Post();
        post.setCaption(dto.getCaption());
        post.setImageUrl(dto.getImageUrl());
        post.setLocation(dto.getLocation());
        return post;
    }

    private PostDto mapToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setCaption(post.getCaption());
        dto.setImageUrl(post.getImageUrl());
        dto.setLocation(post.getLocation());
        dto.setCreatedAt(post.getCreatedAt());
        if(post.getUser() != null) {
            dto.setUsername(post.getUser().getUsername());
        }
        return dto;
    }

    @Override
    public PostDto createPost(PostDto postDto, String username) {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new RuntimeException("User not found ");
        }
        Post post = mapToEntity(postDto);
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }
}
