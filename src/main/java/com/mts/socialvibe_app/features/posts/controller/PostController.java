package com.mts.socialvibe_app.features.posts.controller;

import com.mts.socialvibe_app.features.posts.dto.PostDto;
import com.mts.socialvibe_app.features.posts.service.IPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final IPostService service;

    public PostController(IPostService service) {
        this.service = service;
    }

    @PostMapping("/create-post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.createPost(postDto,username), HttpStatus.CREATED);
    }
}
