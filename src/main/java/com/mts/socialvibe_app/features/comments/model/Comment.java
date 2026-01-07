package com.mts.socialvibe_app.features.comments.model;

import com.mts.socialvibe_app.features.comments.dto.CommentRequest;
import com.mts.socialvibe_app.features.posts.model.Post;
import com.mts.socialvibe_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_tbl")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public static Comment mapToEntity(CommentRequest dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }
}
