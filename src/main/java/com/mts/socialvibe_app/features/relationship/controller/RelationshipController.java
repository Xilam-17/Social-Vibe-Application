package com.mts.socialvibe_app.features.relationship.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.relationship.service.IRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class RelationshipController extends BaseController {

    private final IRelationshipService service;

    @PostMapping("/{targetUserId}")
    public ResponseWrapper toggleFollow(
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        String followerUsername = userDetails.getUsername();
        return createResponse(MessageCode.FOLLOW_API_SUCCESS, service.toggleFollow(followerUsername, targetUserId));
    }

}
