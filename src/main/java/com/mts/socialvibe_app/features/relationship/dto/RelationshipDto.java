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
    private String message;

    private Long followersCount;
    private Long followingsCount;

    public static RelationshipDto from(boolean isFollowing, String message, Long followersCount, Long followingsCount) {
        return RelationshipDto.builder()
                .isFollowing(isFollowing)
                .message(message)
                .followersCount(followersCount)
                .followingsCount(followingsCount)
                .build();
    }
}



