package com.mts.socialvibe_app.user.dto;

import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.user.model.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private Long followerCount;
    private Long followingCount;

    private boolean isFollowing;
    private List<PostResponse> posts;

    public static UserProfileResponse mapToUserProfileResponse(UserEntity user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .build();
    }
}
