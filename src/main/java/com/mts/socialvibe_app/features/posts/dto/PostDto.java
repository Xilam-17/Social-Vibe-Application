package com.mts.socialvibe_app.features.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String caption;
    private String imageUrl;
    private String location;
    private LocalDateTime createdAt;

    private String username;
}
