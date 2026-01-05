package com.mts.socialvibe_app.features.posts.controller;

import com.mts.socialvibe_app.features.posts.dto.PostDto;
import com.mts.socialvibe_app.features.posts.service.IPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all-posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return new ResponseEntity<>(service.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/get-my-posts")
    public ResponseEntity<List<PostDto>> getMyPosts(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.getMyPosts(username), HttpStatus.OK);
    }

    @PutMapping("/edit-post/{id}")
    public ResponseEntity<PostDto> editPost(
            @PathVariable Long id,
            @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.editPost(id, username, postDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.deletePost(id, userDetails.getUsername());
        return new ResponseEntity<>("Post deleted successfully!", HttpStatus.OK);
    }
}
