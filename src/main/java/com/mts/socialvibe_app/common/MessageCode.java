package com.mts.socialvibe_app.common;

import lombok.Getter;

@Getter
public enum MessageCode {

    POST_CREATE_SUCCESS("PCS","Post Created Successfully."),
    POSTS_RETRIEVE_SUCCESS("PRS","Posts retrieved successfully."),
    POST_UPDATE_SUCCESS("PUS", "Post updated successfully."),
    POST_DELETE_SUCCESS("PDS", "Post deleted successfully."),
    COMMENT_CREATE_SUCCESS("CCS", "Comment created successfully"),
    COMMENT_RETRIEVE_SUCCESS("CRS", "Comment retrieved successfully"),
    COMMENT_DELETE_SUCCESS("CDS", "Comment deleted successfully"),
    LIKE_POST_SUCCESS("LTS", "Like a post successfully"),
    USER_REGISTER_SUCCESS("URS", "User registered successfully"),
    USER_LOGIN_SUCCESS("ULS", "User login successfully"),
    UNAUTHORIZED_INVALID_CREDENTIALS("UIC", "Invalid credentials"),
    FOLLOW_API_SUCCESS("FAS", "Follow api success"),
    USER_FOUND_SUCCESS("UFS", "User found success"),
    USER_NOT_FOUND("UNF", "User not found"),
    NOTIFICATION_RETRIEVE_SUCCESS("NRS", "Notification retrieved successfully"),
    NOTIFICATION_COUNT_SUCCESS("NCS", "Notification count success"),
    NOTIFICATION_UPDATE_SUCCESS("NUS", "Notification updated successfully"),
    UPDATE_AVATAR_SUCCESS("UAS", "User Avatar updated successfully"),
    USER_PROFILE_UPDATE_SUCCESS("UPUS", "User profile updated successfully"),
    FRIEND_REQUEST_CONFIRMED("FRC", "Friend request confirmed successfully"),
    FRIEND_REQUEST_DECLINED("FRD", "Friend request declined"),
    POST_SAVE_SUCCESS("PSS", "Post saved successfully"),
    POST_UNSAVE_SUCCESS("PUSS", "Post unsaved successfully");

    private final String statusCode;
    private final String message;

     MessageCode(String statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }


}
