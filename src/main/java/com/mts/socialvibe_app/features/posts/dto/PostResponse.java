package com.mts.socialvibe_app.features.posts.dto;

import com.mts.socialvibe_app.features.posts.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String caption;
    private String imageUrl;
    private String location;
    private LocalDateTime createdAt;

    private String username;
    private String userAvatar;

    private Long likeCount;
    private Long commentCount;
    private boolean isLikedByCurrentUser;

    public static PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .imageUrl(post.getImageUrl())
                .location(post.getLocation())
                .createdAt(post.getCreatedAt())
                .username(post.getUser() != null ? post.getUser().getUsername() : null)
                .userAvatar(post.getUser() != null ? post.getUser().getAvatarUrl() : null)
                .likeCount(post.getLikes() != null ? (long) post.getLikes().size() : 0L)
                .commentCount(post.getComments() != null ? (long) post.getComments().size() : 0L)
                .build();
    }

}
