package com.mts.socialvibe_app.features.posts.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.features.posts.service.IPostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController extends BaseController {

    private final IPostService service;

    public PostController(IPostService service) {
        this.service = service;
    }

    @PostMapping("/create-post")
    public ResponseWrapper createPost(@Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.POST_CREATE_SUCCESS, service.createPost(postRequest,username));
    }

    @GetMapping("/get-all-posts")
    public ResponseWrapper getAllPosts(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.POSTS_RETRIEVE_SUCCESS, service.getAllPosts(username));
    }

    @GetMapping("/get-my-posts")
    public ResponseWrapper getMyPosts(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.POSTS_RETRIEVE_SUCCESS, service.getMyPosts(username));
    }

    @PutMapping("/edit-post/{id}")
    public ResponseWrapper editPost(
            @PathVariable Long id,
            @RequestBody PostRequest postRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.POST_UPDATE_SUCCESS, service.editPost(id, username, postRequest));
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseWrapper deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.deletePost(id, userDetails.getUsername());
        return createResponse(MessageCode.POST_DELETE_SUCCESS, "Post Deleted");
    }

    @GetMapping("/new-feed")
    public ResponseEntity<List<PostResponse>> getFeed(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.getFeed(username), HttpStatus.OK);
    }
}
