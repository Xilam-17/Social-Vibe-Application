package com.mts.socialvibe_app.features.relationship.service;

import com.mts.socialvibe_app.features.notifications.model.NotificationType;
import com.mts.socialvibe_app.features.notifications.service.NotificationService;
import com.mts.socialvibe_app.features.relationship.dto.RelationshipDto;
import com.mts.socialvibe_app.features.relationship.model.Relationship;
import com.mts.socialvibe_app.features.relationship.repository.RelationshipRepository;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RelationshipService implements IRelationshipService {

    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final NotificationService notificationService;

    @Override
    public RelationshipDto toggleFollow(String followerUsername, Long targetUserId) {

        UserEntity follower = userRepository.findByUsername(followerUsername);
        UserEntity following = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (follower.getId().equals(targetUserId)) {
            throw new RuntimeException("You can't follow yourself!");
        }

        Optional<Relationship> existingFollow = relationshipRepository.findByFollowerUsernameAndFollowingId(follower.getUsername(), following.getId());
        boolean isFollowing;
        String initialMessage;

        if (existingFollow.isPresent()) {
            relationshipRepository.delete(existingFollow.get());
            isFollowing = false;
            initialMessage = "Unfollowed " + following.getUsername();
        } else {
            Relationship relationship = new Relationship();
            relationship.setFollower(follower);
            relationship.setFollowing(following);
            relationshipRepository.save(relationship);
            notificationService.createNotification(following, follower, NotificationType.FOLLOW, null);
            isFollowing = true;
            initialMessage = "Followed " + following.getUsername();
        }

        relationshipRepository.flush();

        boolean targetFollowsMe = relationshipRepository.isFollowingBack(follower.getId(), targetUserId);
        boolean isFriend = isFollowing && targetFollowsMe;

        Long followersCount = relationshipRepository.countFollowerByFollowingId(targetUserId);
        Long followingsCount = relationshipRepository.countFollowingByFollowerId(targetUserId);

        String finalMessage = isFriend ? "You are now friends with " + following.getUsername() : initialMessage;

        return RelationshipDto.from(isFollowing, isFriend, finalMessage, followersCount, followingsCount);
    }
}