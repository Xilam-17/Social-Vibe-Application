package com.mts.socialvibe_app.features.posts.service;

import com.mts.socialvibe_app.features.posts.dto.PostDto;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
        return post;
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

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<PostDto> getMyPosts(String username) {
        List<Post> posts = postRepository.findByUserUsernameOrderByCreatedAtDesc(username);
        return posts.stream().map(this::mapToDto).toList();
    }

    @Override
    public PostDto editPost(Long id, String username, PostDto postDto) {
        Post post = getPost(id);

        if(!post.getUser().getUsername().equals(username)){
            throw new RuntimeException("You are not authorized to edit this post!");
        }

        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id, String username) {
        Post post = getPost(id);
        if(!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        postRepository.delete(post);
    }


}
