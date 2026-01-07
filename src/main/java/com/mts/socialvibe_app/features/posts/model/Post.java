package com.mts.socialvibe_app.features.posts.model;

import com.mts.socialvibe_app.features.comments.model.Comment;
import com.mts.socialvibe_app.features.likes.model.Like;
import com.mts.socialvibe_app.features.posts.dto.PostRequest;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String caption;

    private String imageUrl;
    private String location;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments;

    public static Post mapToEntity(PostRequest dto) {
        Post post = new Post();
        post.setCaption(dto.getCaption());
        post.setImageUrl(dto.getImgUrl());
        post.setLocation(dto.getLocation());
        return post;
    }
}
