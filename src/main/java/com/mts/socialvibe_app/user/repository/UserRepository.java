package com.mts.socialvibe_app.user.repository;

import com.mts.socialvibe_app.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    @Query("select r.following from Relationship r where r.follower.id = :follower_id")
    List<UserEntity> findFollowingUserListByFollowerId(@Param("follower_id") Long followerId);

    @Query("select r.follower from Relationship r where r.following.id = :following_id")
    List<UserEntity> findFollowerUserListByFollowingId(@Param("following_id") Long followingId);

    @Query("select u from UserEntity u where lower(u.username) like lower(concat('%', :targetUsername, '%')) or" +
            " lower(u.fullName) like lower(concat('%', :targetUsername, '%')) and u.username != :currentUsername")
    List<UserEntity> searchUser(@Param("targetUsername") String targetUsername,@Param("currentUsername") String currentUsername);

}
