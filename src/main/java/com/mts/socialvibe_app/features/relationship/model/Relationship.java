package com.mts.socialvibe_app.features.relationship.model;

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
@Table(name = "relationship_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private UserEntity following;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
