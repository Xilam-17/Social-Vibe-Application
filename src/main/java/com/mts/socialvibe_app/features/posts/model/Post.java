package com.mts.socialvibe_app.features.posts.model;

import com.mts.socialvibe_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
}
