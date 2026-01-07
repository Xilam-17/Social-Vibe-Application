package com.mts.socialvibe_app.features.comments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment content cannot be empty!")
    private String content;
}
