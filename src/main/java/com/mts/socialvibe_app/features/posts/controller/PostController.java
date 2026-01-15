package com.mts.socialvibe_app.features.posts.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.service.IPostService;
import com.mts.socialvibe_app.user.service.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController extends BaseController {

    private final IPostService service;

    public PostController(IPostService service) {
        this.service = service;
    }

    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseWrapper createPost(
            @RequestPart("postData") @Valid PostRequest postRequest,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        String username = userDetails.getUsername();

        return createResponse(MessageCode.POST_CREATE_SUCCESS, service.createPost(postRequest, file, username));
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

    @GetMapping("/feed")
    public ResponseWrapper getFollowingFeed(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return createResponse(MessageCode.POSTS_RETRIEVE_SUCCESS,
                service.getFollowingFeed(userPrincipal.getId(), userPrincipal.getUsername(), page, size));
    }

    @PutMapping(value = "/edit-post/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseWrapper editPost(
            @PathVariable("id") Long id,
            @RequestPart("postData") PostRequest postRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.POST_UPDATE_SUCCESS, service.editPost(id, username, postRequest, file));
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseWrapper deletePost(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        service.deletePost(id, userDetails.getUsername());
        return createResponse(MessageCode.POST_DELETE_SUCCESS, "Post Deleted");
    }

}
