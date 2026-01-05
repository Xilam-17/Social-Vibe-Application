package com.mts.socialvibe_app.features.likes.controller;

import com.mts.socialvibe_app.features.likes.dto.LikeDto;
import com.mts.socialvibe_app.features.likes.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LikeController {

    private final ILikeService service;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeDto> toggleLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.toggleLike(postId, username), HttpStatus.OK);
    }

}
