package com.mts.socialvibe_app.features.comments.service;

import com.mts.socialvibe_app.features.comments.dto.CommentDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ICommentService {
    CommentDto createComment(Long postId, String username, CommentDto commentDto);

    List<CommentDto> getAllCommentsByPostId(Long postId);

    void deleteComment(Long postId, Long commentId, UserDetails userDetails);
}
