package com.mts.socialvibe_app.features.likes.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.likes.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LikeController extends BaseController {

    private final ILikeService service;

    @PostMapping("/{postId}/like")
    public ResponseWrapper toggleLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.LIKE_TOGGLE_SUCCESS, service.toggleLike(postId, username));
    }

}
