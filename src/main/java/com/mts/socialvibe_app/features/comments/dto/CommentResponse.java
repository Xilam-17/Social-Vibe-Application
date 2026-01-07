package com.mts.socialvibe_app.features.comments.dto;

import com.mts.socialvibe_app.features.comments.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String userAvatar;

    public static CommentResponse mapToDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .userAvatar(comment.getUser() != null ? comment.getUser().getAvatarUrl() : null)
                .build();
    }
}
