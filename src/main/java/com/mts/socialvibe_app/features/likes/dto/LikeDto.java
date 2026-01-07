package com.mts.socialvibe_app.features.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {
    private Long countLike;
    private boolean isLiked;
    private String message;
}
