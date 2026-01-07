package com.mts.socialvibe_app.features.comments.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.features.comments.dto.CommentRequest;
import com.mts.socialvibe_app.features.comments.service.ICommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController extends BaseController {

    private final ICommentService service;

    public CommentController(ICommentService service) {
        this.service = service;
    }

    @PostMapping("/{postId}/comment")
    public ResponseWrapper createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentRequest commentRequest) {
        String username = userDetails.getUsername();
        return createResponse(MessageCode.COMMENT_CREATE_SUCCESS, service.createComment(postId, username, commentRequest));
    }

    @GetMapping("/{postId}/comments")
    public ResponseWrapper getAllComments(@PathVariable Long postId) {
        return  createResponse(MessageCode.COMMENT_RETRIEVE_SUCCESS, service.getAllCommentsByPostId(postId));
    }

    @DeleteMapping("/{postId}/comment/{commentId}/delete-comment")
    public ResponseWrapper deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        service.deleteComment(postId, commentId, username);
        return createResponse(MessageCode.COMMENT_DELETE_SUCCESS, "Comment Deleted");
    }
}
