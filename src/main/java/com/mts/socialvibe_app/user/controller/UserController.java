package com.mts.socialvibe_app.user.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.user.dto.request.UserRegisterRequest;
import com.mts.socialvibe_app.user.dto.request.UserRequest;
import com.mts.socialvibe_app.user.service.IUserService;
import com.mts.socialvibe_app.user.service.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseWrapper register(@Valid @RequestBody UserRegisterRequest userRequest) {
        return createResponse(MessageCode.USER_REGISTER_SUCCESS, service.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseWrapper login(@RequestBody UserRequest userRequest) {
        try {
            String token = service.verify(userRequest);
            if ("Fail".equals(token)) {
                return createResponse(MessageCode.UNAUTHORIZED_INVALID_CREDENTIALS, "Authentication failed");
            }
            return createResponse(MessageCode.USER_LOGIN_SUCCESS, token);
        } catch (Exception e) {
            return createResponse(MessageCode.UNAUTHORIZED_INVALID_CREDENTIALS, e.getMessage());
        }
    }

    @GetMapping("/search-user")
    public ResponseWrapper searchUser(@RequestParam String targetUsername, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.searchUser(targetUsername, currentUsername));
    }


    @GetMapping("/profile/{targetUsername}")
    public ResponseWrapper getUserProfile(@PathVariable String targetUsername, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.getUserProfile(targetUsername, currentUsername));
    }

    @GetMapping("/followings")
    public ResponseWrapper followingsUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.followingsUsers(userId));
    }

    @GetMapping("/followers")
    public ResponseWrapper followersUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.followersUsers(userId));
    }

    @PostMapping("/avatar")
    public ResponseWrapper updateAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return createResponse(MessageCode.UPDATE_AVATAR_SUCCESS, service.updateAvatar(userDetails.getUsername(), file));
    }
}
