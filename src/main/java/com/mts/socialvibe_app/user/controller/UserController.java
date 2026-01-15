package com.mts.socialvibe_app.user.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.user.dto.request.ConfirmFriendRequest;
import com.mts.socialvibe_app.user.dto.request.EditProfileRequest;
import com.mts.socialvibe_app.user.dto.request.UserRegisterRequest;
import com.mts.socialvibe_app.user.dto.request.UserRequest;
import com.mts.socialvibe_app.user.service.IUserService;
import com.mts.socialvibe_app.user.service.UserPrincipal;

import jakarta.validation.Valid;

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
    public ResponseWrapper searchUser(@RequestParam("targetUsername") String targetUsername, @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.searchUser(targetUsername, currentUsername));
    }


    @GetMapping("/profile/{targetUsername}")
    public ResponseWrapper getUserProfile(@PathVariable("targetUsername") String targetUsername, @AuthenticationPrincipal UserDetails userDetails) {
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
        String currentUsername = userPrincipal.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.followersUsers(userId, currentUsername));
    }

    @GetMapping("/friends")
    public ResponseWrapper friendsList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        String currentUsername = userPrincipal.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.getFriendsList(userId, currentUsername));
    }

    @GetMapping("/suggest-friends")
    public ResponseWrapper suggestFriends(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        String currentUsername = userPrincipal.getUsername();
        return createResponse(MessageCode.USER_FOUND_SUCCESS, service.suggestFriends(userId, currentUsername));
    }

    @PostMapping("/confirm-friend-request")
    public ResponseWrapper confirmFriendRequest(
            @Valid @RequestBody ConfirmFriendRequest confirmFriendRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        com.mts.socialvibe_app.features.relationship.dto.RelationshipDto result = 
                service.confirmFriendRequest(currentUsername, confirmFriendRequest);
        
        MessageCode messageCode = confirmFriendRequest.getConfirm() 
                ? MessageCode.FRIEND_REQUEST_CONFIRMED 
                : MessageCode.FRIEND_REQUEST_DECLINED;
        
        return createResponse(messageCode, result);
    }

    @PostMapping("/avatar")
    public ResponseWrapper updateAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return createResponse(MessageCode.UPDATE_AVATAR_SUCCESS, service.updateAvatar(userDetails.getUsername(), file));
    }

    @PutMapping(value = "/edit-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseWrapper editProfile(
            @RequestPart("profileData") @Valid EditProfileRequest editProfileRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return createResponse(MessageCode.USER_PROFILE_UPDATE_SUCCESS, 
                service.editProfile(userDetails.getUsername(), editProfileRequest, file));
    }
}
