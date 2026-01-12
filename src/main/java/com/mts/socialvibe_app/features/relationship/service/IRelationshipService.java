package com.mts.socialvibe_app.features.relationship.service;

import com.mts.socialvibe_app.features.relationship.dto.RelationshipDto;

public interface IRelationshipService {
    RelationshipDto toggleFollow(String followerUsername, Long targetUserId);
}
