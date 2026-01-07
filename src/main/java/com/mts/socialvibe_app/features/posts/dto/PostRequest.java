package com.mts.socialvibe_app.features.posts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostRequest {
    @NotBlank(message = "Caption cannot be empty")
    private String caption;
    private String imgUrl;
    private String location;
}
