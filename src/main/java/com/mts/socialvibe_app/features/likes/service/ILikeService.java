package com.mts.socialvibe_app.features.likes.service;

import com.mts.socialvibe_app.features.likes.dto.LikeDto;

public interface ILikeService {
    LikeDto toggleLike(Long postId, String username);
}
