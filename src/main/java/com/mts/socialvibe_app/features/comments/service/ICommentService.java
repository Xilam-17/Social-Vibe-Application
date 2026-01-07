package com.mts.socialvibe_app.features.comments.service;

import com.mts.socialvibe_app.features.comments.dto.CommentRequest;
import com.mts.socialvibe_app.features.comments.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    CommentResponse createComment(Long postId, String username, CommentRequest commentRequest);

    List<CommentResponse> getAllCommentsByPostId(Long postId);

    void deleteComment(Long postId, Long commentId, String username);
}
