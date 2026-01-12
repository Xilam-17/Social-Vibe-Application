package com.mts.socialvibe_app.features.relationship.service;

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

    @Override
    public RelationshipDto toggleFollow(String followerUsername, Long targetUserId) {

        UserEntity follower = userRepository.findByUsername(followerUsername);
        UserEntity following = userRepository.findById(targetUserId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(follower.getId().equals(targetUserId)) {
            throw new RuntimeException("You can't follow yourself!");
        }

        Optional<Relationship> existingFollow = relationshipRepository.findByFollowerUsernameAndFollowingId(follower.getUsername(), following.getId());
        boolean isFollowing;
        String message;

        if(existingFollow.isPresent()) {
            relationshipRepository.delete(existingFollow.get());
            relationshipRepository.flush();
            isFollowing = false;
            message = "Unfollowed " + following.getUsername();
        } else {
            Relationship relationship = new Relationship();
            relationship.setFollower(follower);
            relationship.setFollowing(following);
            relationshipRepository.save(relationship);
            relationshipRepository.flush();
            isFollowing = true;
            message = "Followed " + following.getUsername();
        }

        relationshipRepository.flush();

        Long followersCount = relationshipRepository.countFollowingByFollowerId(targetUserId);
        Long followingsCount = relationshipRepository.countFollowerByFollowingId(targetUserId);

        return RelationshipDto.from(isFollowing, message, followersCount, followingsCount);
    }
}
