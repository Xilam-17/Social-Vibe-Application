package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.user.dto.request.UserRegisterRequest;
import com.mts.socialvibe_app.user.dto.response.UserProfileResponse;
import com.mts.socialvibe_app.user.dto.request.UserRequest;
import com.mts.socialvibe_app.user.dto.response.UserRegisterResponse;
import com.mts.socialvibe_app.user.dto.response.UserResponse;
import com.mts.socialvibe_app.user.dto.response.UserSearchResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    UserRegisterResponse register(UserRegisterRequest userRequest);

    String verify(UserRequest userRequest);

    List<UserSearchResponse> searchUser(String targetUsername, String currentUsername);

    List<UserResponse> followingsUsers(Long userId);

    List<UserResponse> followersUsers(Long userId);

    UserProfileResponse getUserProfile(String targetUsername, String currentUsername);

    UserResponse updateAvatar(String username, MultipartFile file) throws IOException;
}
