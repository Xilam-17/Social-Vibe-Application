package com.mts.socialvibe_app.common;

public enum MessageCode {

    POST_CREATE_SUCCESS("PCS","Post Created Successfully."),
    POSTS_RETRIEVE_SUCCESS("PRS","Posts retrieved successfully."),
    POST_UPDATE_SUCCESS("PUS", "Post updated successfully."),
    POST_DELETE_SUCCESS("PDS", "Post deleted successfully."),
    COMMENT_CREATE_SUCCESS("CCS", "Comment created successfully"),
    COMMENT_RETRIEVE_SUCCESS("CRS", "Comment retrieved successfully"),
    COMMENT_DELETE_SUCCESS("CDS", "Comment deleted successfully"),
    LIKE_TOGGLE_SUCCESS("LTS", "Like toggled successfully"),
    USER_REGISTER_SUCCESS("URS", "User registered successfully"),
    USER_LOGIN_SUCCESS("ULS", "User login successfully"),
    UNAUTHORIZED_INVALID_CREDENTIALS("UIC", "Invalid credentials");
    private final String statusCode;
    private final String message;

     MessageCode(String statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCode(){
         return statusCode;
    }

    public String getMessage(){
         return message;
    }


}
