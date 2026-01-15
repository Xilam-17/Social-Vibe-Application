package com.mts.socialvibe_app.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mts.socialvibe_app.common.LocalStorageService;
import com.mts.socialvibe_app.config.jwt.JwtService;
import com.mts.socialvibe_app.features.posts.dto.PostResponse;
import com.mts.socialvibe_app.features.posts.repository.PostRepository;
import com.mts.socialvibe_app.features.posts.service.PostService;
import com.mts.socialvibe_app.features.notifications.model.NotificationType;
import com.mts.socialvibe_app.features.notifications.service.NotificationService;
import com.mts.socialvibe_app.features.relationship.dto.RelationshipDto;
import com.mts.socialvibe_app.features.relationship.model.Relationship;
import com.mts.socialvibe_app.features.relationship.repository.RelationshipRepository;
import java.util.Optional;
import com.mts.socialvibe_app.user.dto.request.ConfirmFriendRequest;
import com.mts.socialvibe_app.user.dto.request.EditProfileRequest;
import com.mts.socialvibe_app.user.dto.request.UserRegisterRequest;
import com.mts.socialvibe_app.user.dto.request.UserRequest;
import com.mts.socialvibe_app.user.dto.response.UserProfileResponse;
import com.mts.socialvibe_app.user.dto.response.UserRegisterResponse;
import com.mts.socialvibe_app.user.dto.response.UserResponse;
import com.mts.socialvibe_app.user.dto.response.UserSearchResponse;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RelationshipRepository relationshipRepository;
    private final PostService postService;
    private final PostRepository postRepository;
    private final LocalStorageService localStorageService;
    private final NotificationService notificationService;


    @Override
    public UserRegisterResponse register(UserRegisterRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username '" + userRequest.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email '" + userRequest.getEmail() + "' is already registered.");
        }

        UserEntity user = UserEntity.mapToEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return UserRegisterResponse.mapToDto(savedUser);
    }

    @Override
    public String verify(UserRequest userRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            UserEntity user = userRepository.findByUsernameOrEmail(userRequest.getUsername(), userRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return jwtService.generateToken(user.getUsername());
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
    public List<UserResponse> followersUsers(Long userId, String currentUsername) {
        List<UserEntity> followers = userRepository.findFollowerUserListByFollowingId(userId);
        return followers.stream().map(follower -> {
            UserResponse dto = convertToUserResponse(follower);
            boolean isFollowing = relationshipRepository.findByFollowerUsernameAndFollowingId(currentUsername, follower.getId()).isPresent();
            dto.setIsFollowing(isFollowing);
            return dto;
        }).toList();
    }

    @Override
    public UserProfileResponse getUserProfile(String targetUsername, String currentUsername) {
        UserEntity targetUser = userRepository.findByUsername(targetUsername);
        if(targetUser == null) throw new RuntimeException("User not found");

        Long followers = relationshipRepository.countFollowerByFollowingId(targetUser.getId());
        Long followings = relationshipRepository.countFollowingByFollowerId(targetUser.getId());

        Long postCount = postRepository.countByUserId(targetUser.getId());

        boolean isFollowing = relationshipRepository.findByFollowerUsernameAndFollowingId(currentUsername, targetUser.getId()).isPresent();
        List<PostResponse> userPosts = postService.getPostsByUsername(targetUsername, currentUsername);

        return UserProfileResponse.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .fullName(targetUser.getFullName())
                .email(targetUser.getEmail())
                .avatarUrl(targetUser.getAvatarUrl())
                .bio(targetUser.getBio())
                .postCount(postCount)
                .followerCount(followers)
                .followingCount(followings)
                .isFollowing(isFollowing)
                .posts(userPosts)
                .build();
    }

    @Transactional
    public UserResponse updateAvatar(String username, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        String imageUri = localStorageService.saveFile(file);

        if (user.getAvatarUrl() != null) {
            localStorageService.deleteFile(user.getAvatarUrl());
        }

        user.setAvatarUrl(imageUri);
        return UserResponse.mapToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse editProfile(String username, EditProfileRequest editProfileRequest, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        // Update username if provided and different from current
        if (editProfileRequest.getUsername() != null && !editProfileRequest.getUsername().trim().isEmpty()) {
            String newUsername = editProfileRequest.getUsername().trim();
            if (!newUsername.equals(user.getUsername())) {
                // Check if new username is already taken
                if (userRepository.existsByUsername(newUsername)) {
                    throw new RuntimeException("Username '" + newUsername + "' is already taken.");
                }
                user.setUsername(newUsername);
            }
        }

        // Update bio if provided
        if (editProfileRequest.getBio() != null) {
            user.setBio(editProfileRequest.getBio().trim());
        }

        // Update avatar if file is provided
        if (file != null && !file.isEmpty()) {
            String imageUri = localStorageService.saveFile(file);
            
            // Delete old avatar if it exists
            if (user.getAvatarUrl() != null) {
                localStorageService.deleteFile(user.getAvatarUrl());
            }
            
            user.setAvatarUrl(imageUri);
        }

        return UserResponse.mapToDto(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getFriendsList(Long userId, String currentUsername) {
        List<UserEntity> friends = relationshipRepository.findMutualFollows(userId);
        return friends.stream().map(friend -> {
            UserResponse dto = convertToUserResponse(friend);
            // For friends, isFollowing should always be true since it's mutual
            dto.setIsFollowing(true);
            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public RelationshipDto confirmFriendRequest(String currentUsername, ConfirmFriendRequest confirmFriendRequest) {
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        UserEntity targetUser = userRepository.findById(confirmFriendRequest.getTargetUserId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (currentUser.getId().equals(confirmFriendRequest.getTargetUserId())) {
            throw new RuntimeException("You cannot confirm a friend request with yourself!");
        }

        // Check if target user is following the current user
        boolean targetIsFollowingMe = relationshipRepository.isFollowingBack(
                currentUser.getId(), 
                confirmFriendRequest.getTargetUserId()
        );

        if (!targetIsFollowingMe) {
            throw new RuntimeException("This user is not following you. Cannot confirm friend request.");
        }

        // Check if current user already follows target user
        Optional<Relationship> existingFollow = relationshipRepository.findByFollowerUsernameAndFollowingId(
                currentUsername, 
                confirmFriendRequest.getTargetUserId()
        );

        boolean isFollowing;
        boolean isFriend;
        String message;
        
        if (confirmFriendRequest.getConfirm()) {
            // User wants to follow back (confirm)
            if (existingFollow.isPresent()) {
                // Already following, so they're already friends
                isFollowing = true;
                isFriend = true;
                message = "You are already friends with " + targetUser.getUsername();
            } else {
                // Create the follow relationship (follow back)
                Relationship relationship = new Relationship();
                relationship.setFollower(currentUser);
                relationship.setFollowing(targetUser);
                relationshipRepository.save(relationship);
                
                isFollowing = true;
                isFriend = true;
                message = "You are now friends with " + targetUser.getUsername();
            }
        } else {
            // User declines (doesn't want to follow back)
            if (existingFollow.isPresent()) {
                // If they were already following, remove the relationship
                relationshipRepository.delete(existingFollow.get());
                isFollowing = false;
                isFriend = false;
                message = "Friend request declined. Unfollowed " + targetUser.getUsername();
            } else {
                // Just decline, don't follow back
                isFollowing = false;
                isFriend = false;
                message = "Friend request declined";
            }
        }

        relationshipRepository.flush();

        Long followersCount = relationshipRepository.countFollowerByFollowingId(confirmFriendRequest.getTargetUserId());
        Long followingsCount = relationshipRepository.countFollowingByFollowerId(confirmFriendRequest.getTargetUserId());

        return RelationshipDto.from(isFollowing, isFriend, message, followersCount, followingsCount);
    }

    @Override
    public List<UserResponse> suggestFriends(Long userId, String currentUsername) {
        // Get all my friends (mutual follows)
        List<UserEntity> myFriends = relationshipRepository.findMutualFollows(userId);
        
        // Get friends of my friends who are not already my friends
        List<UserEntity> suggestedFriends = myFriends.stream()
                .flatMap(friend -> relationshipRepository.findFriendsOfUser(friend.getId()).stream())
                .filter(suggestedUser -> {
                    // Exclude myself
                    if (suggestedUser.getId().equals(userId)) {
                        return false;
                    }
                    // Exclude users who are already my friends
                    return !relationshipRepository.findMutualFollows(userId).contains(suggestedUser);
                })
                .distinct() // Remove duplicates
                .toList();
        
        // Convert to UserResponse with isFollowing status
        return suggestedFriends.stream().map(suggestedUser -> {
            UserResponse dto = convertToUserResponse(suggestedUser);
            // Check if current user is following this suggested user
            boolean isFollowing = relationshipRepository.findByFollowerUsernameAndFollowingId(
                    currentUsername, 
                    suggestedUser.getId()
            ).isPresent();
            dto.setIsFollowing(isFollowing);
            return dto;
        }).toList();
    }

    private UserResponse convertToUserResponse(UserEntity user) {
        UserResponse dto = UserResponse.mapToDto(user);
        dto.setPostCount(postRepository.countByUserId(user.getId()));
        dto.setFollowerCount(relationshipRepository.countFollowerByFollowingId(user.getId()));
        dto.setFollowingCount(relationshipRepository.countFollowingByFollowerId(user.getId()));
        return dto;
    }


}