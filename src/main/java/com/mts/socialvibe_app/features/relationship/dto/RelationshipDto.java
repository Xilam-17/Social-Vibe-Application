package com.mts.socialvibe_app.features.relationship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipDto {
    private boolean isFollowing;
    private boolean isFriend;
    private String message;
    private Long followersCount;
    private Long followingsCount;

    public static RelationshipDto from(boolean isFollowing,boolean isFriend, String message, Long followersCount, Long followingsCount) {
        return RelationshipDto.builder()
                .isFollowing(isFollowing)
                .isFriend(isFriend)
                .message(message)
                .followersCount(followersCount)
                .followingsCount(followingsCount)
                .build();
    }
}



