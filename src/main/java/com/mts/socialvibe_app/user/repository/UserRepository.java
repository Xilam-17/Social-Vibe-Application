package com.mts.socialvibe_app.user.repository;

import com.mts.socialvibe_app.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
