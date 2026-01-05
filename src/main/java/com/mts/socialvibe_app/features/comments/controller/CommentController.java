package com.mts.socialvibe_app.features.comments.controller;

import com.mts.socialvibe_app.features.comments.dto.CommentDto;
import com.mts.socialvibe_app.features.comments.service.ICommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    private final ICommentService service;

    public CommentController(ICommentService service) {
        this.service = service;
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentDto commentDto) {
        String username = userDetails.getUsername();
        return new ResponseEntity<>(service.createComment(postId, username, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable Long postId) {
        return new ResponseEntity<>(service.getAllCommentsByPostId(postId), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comment/{commentId}/delete-comment")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteComment(postId, commentId, userDetails);
        return new ResponseEntity<>("Comment deleted successfully!", HttpStatus.OK);
    }
}
