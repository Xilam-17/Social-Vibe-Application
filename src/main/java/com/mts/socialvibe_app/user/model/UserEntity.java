package com.mts.socialvibe_app.user.model;

import com.mts.socialvibe_app.features.comments.model.Comment;
import com.mts.socialvibe_app.features.likes.model.Like;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.user.dto.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Column(unique = true)
    private String email;
    private String avatarUrl;

    @Column(length = 500)
    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments;

    public static UserEntity mapToEntity(UserRequest dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());
        return user;
    }
}
