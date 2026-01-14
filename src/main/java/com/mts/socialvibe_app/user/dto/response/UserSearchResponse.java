package com.mts.socialvibe_app.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchResponse {
    private Long id;
    private String username;
    private String fullName;
    private String avatarUrl;
    private boolean isFollowing;
}
