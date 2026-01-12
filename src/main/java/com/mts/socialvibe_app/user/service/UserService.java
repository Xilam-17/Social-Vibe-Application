package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.features.posts.service.PostService;
import com.mts.socialvibe_app.features.relationship.repository.RelationshipRepository;
import com.mts.socialvibe_app.filters.jwt.JwtService;
import com.mts.socialvibe_app.user.dto.UserProfileResponse;
import com.mts.socialvibe_app.user.dto.UserRequest;
import com.mts.socialvibe_app.user.dto.UserResponse;
import com.mts.socialvibe_app.user.dto.UserSearchResponse;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RelationshipRepository relationshipRepository;
    private final PostService postService;


    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username '" + userRequest.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email '" + userRequest.getEmail() + "' is already registered.");
        }

        UserEntity user = UserEntity.mapToEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return UserResponse.mapToDto(savedUser);
    }

    @Override
    public String verify(UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userRequest.getUsername());
        }
        return "Fail";
    }

    @Override
    public List<UserSearchResponse> searchUser(String targetUsername, String currentUsername) {
        List<UserEntity> foundUsers = userRepository.searchUser(targetUsername, currentUsername);

        return foundUsers.stream().map(user -> {
          boolean isFollowing = relationshipRepository.findByFollowerUsernameAndFollowingId(currentUsername, user.getId()).isPresent();

                return UserSearchResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarUrl())
                        .isFollowing(isFollowing)
                        .build();
        }).toList();

    }

    @Override
    public List<UserResponse> followingsUsers(Long userId) {
        List<UserEntity> followings = userRepository.findFollowingUserListByFollowerId(userId);
        return followings.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public List<UserResponse> followersUsers(Long userId) {
        List<UserEntity> followers = userRepository.findFollowerUserListByFollowingId(userId);
        return followers.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public UserProfileResponse getUserProfile(String targetUsername, String currentUsername) {

        UserEntity targetUser = userRepository.findByUsername(targetUsername);
        if(targetUser == null) throw new RuntimeException("User not found");

        boolean isFollowing = relationshipRepository.findByFollowerUsernameAndFollowingId(currentUsername, targetUser.getId()).isPresent();

        Long followers = relationshipRepository.countFollowerByFollowingId(targetUser.getId());
        Long followings = relationshipRepository.countFollowingByFollowerId(targetUser.getId());

        List<PostResponse> userPosts = postService.getPostsByUsername(targetUsername, currentUsername);

        return UserProfileResponse.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .fullName(targetUser.getFullName())
                .email(targetUser.getEmail())
                .avatarUrl(targetUser.getAvatarUrl())
                .bio(targetUser.getBio())
                .postCount((long) userPosts.size())
                .followerCount(followers)
                .followingCount(followings)
                .isFollowing(isFollowing)
                .posts(userPosts)
                .build();
    }

    private UserResponse convertToUserResponse(UserEntity user) {
        UserResponse dto = UserResponse.mapToDto(user);

        dto.setPostCount((long) user.getPosts().size());

        dto.setFollowerCount(relationshipRepository.countFollowingByFollowerId(user.getId()));

        dto.setFollowingCount(relationshipRepository.countFollowerByFollowingId(user.getId()));
        return  dto;
    }


}