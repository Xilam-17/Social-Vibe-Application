package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.user.dto.UserProfileResponse;
import com.mts.socialvibe_app.user.dto.UserRequest;
import com.mts.socialvibe_app.user.dto.UserResponse;
import com.mts.socialvibe_app.user.dto.UserSearchResponse;

import java.util.List;

public interface IUserService {
    UserResponse register(UserRequest userRequest);

    String verify(UserRequest userRequest);

    List<UserSearchResponse> searchUser(String targetUsername, String currentUsername);

    List<UserResponse> followingsUsers(Long userId);

    List<UserResponse> followersUsers(Long userId);

    UserProfileResponse getUserProfile(String targetUsername, String currentUsername);
}
