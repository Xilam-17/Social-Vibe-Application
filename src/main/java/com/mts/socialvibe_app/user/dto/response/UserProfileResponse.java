package com.mts.socialvibe_app.user.dto.response;

import java.util.List;

import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.user.model.UserEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String bio;

    private Long postCount;
    private Long savedPostCount;
    private Long followerCount;
    private Long followingCount;

    private boolean isFollowing;
    private List<PostResponse> posts;
    private List<PostResponse> savedPosts;

}
