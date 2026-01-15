# Social Vibe Application - API Documentation

## Base Information

- **Base URL**: `http://localhost:8080`
- **API Version**: `v1`
- **Base Path**: `/api/v1`
- **Authentication**: JWT Bearer Token (required for most endpoints)
- **Content-Type**: `application/json` (unless specified otherwise)

## Response Format

All API responses follow this standard format:

```json
{
  "statusCode": "string",
  "message": "string",
  "data": "object",
  "timeStamp": "2024-01-01T00:00:00"
}
```

---

## 1. User Management APIs

### 1.1 Register User

**Endpoint**: `/api/v1/user/register`  
**Method**: `POST`  
**Authentication**: Not Required

**Request Body**:
```json
{
  "username": "string (3-20 characters, required)",
  "fullName": "string (required)",
  "password": "string (min 8 characters, required)",
  "email": "string (valid email format, required)"
}
```

**Response** (Success - 200):
```json
{
  "statusCode": "URS",
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "johndoe",
    "fullName": "John Doe",
    "email": "john@example.com"
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.2 Login User

**Endpoint**: `/api/v1/user/login`  
**Method**: `POST`  
**Authentication**: Not Required

**Request Body**:
```json
{
  "username": "string (3-20 characters, required)",
  "fullName": "string (required)",
  "password": "string (min 8 characters, required)",
  "email": "string (valid email format, required)",
  "avatarUrl": "string (optional)",
  "bio": "string (optional)"
}
```

**Response** (Success - 200):
```json
{
  "statusCode": "ULS",
  "message": "User login successfully",
  "data": "jwt_token_string",
  "timeStamp": "2024-01-01T12:00:00"
}
```

**Response** (Error - 401):
```json
{
  "statusCode": "UIC",
  "message": "Invalid credentials",
  "data": "Authentication failed",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.3 Search User

**Endpoint**: `/api/v1/user/search-user`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Query Parameters**:
- `targetUsername` (string, required): Username to search for

**Example Request**:
```
GET /api/v1/user/search-user?targetUsername=johndoe
```

**Response** (Success - 200):
```json
{
  "statusCode": "UFS",
  "message": "User found success",
  "data": {
    "id": 1,
    "username": "johndoe",
    "fullName": "John Doe",
    "avatarUrl": "http://localhost:8080/images/avatar.jpg",
    "isFollowing": false
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.4 Get User Profile

**Endpoint**: `/api/v1/user/profile/{targetUsername}`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `targetUsername` (string, required): Username of the profile to retrieve

**Example Request**:
```
GET /api/v1/user/profile/johndoe
```

**Response** (Success - 200):
```json
{
  "statusCode": "UFS",
  "message": "User found success",
  "data": {
    "id": 1,
    "username": "johndoe",
    "fullName": "John Doe",
    "email": "john@example.com",
    "avatarUrl": "http://localhost:8080/images/avatar.jpg",
    "bio": "User bio text",
    "postCount": 10,
    "followerCount": 50,
    "followingCount": 30,
    "isFollowing": true,
    "posts": [
      {
        "id": 1,
        "caption": "Post caption",
        "imageUrl": "http://localhost:8080/images/post.jpg",
        "location": "New York",
        "createdAt": "2024-01-01T10:00:00",
        "username": "johndoe",
        "userAvatar": "http://localhost:8080/images/avatar.jpg",
        "likeCount": 25,
        "commentCount": 5,
        "isLikedByCurrentUser": true
      }
    ]
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.5 Get Followings

**Endpoint**: `/api/v1/user/followings`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "UFS",
  "message": "User found success",
  "data": [
    {
      "id": 2,
      "username": "janedoe",
      "fullName": "Jane Doe",
      "avatarUrl": "http://localhost:8080/images/avatar2.jpg",
      "isFollowing": true
    }
  ],
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.6 Get Followers

**Endpoint**: `/api/v1/user/followers`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "UFS",
  "message": "User found success",
  "data": [
    {
      "id": 3,
      "username": "bobsmith",
      "fullName": "Bob Smith",
      "avatarUrl": "http://localhost:8080/images/avatar3.jpg",
      "isFollowing": false
    }
  ],
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 1.7 Update Avatar

**Endpoint**: `/api/v1/user/avatar`  
**Method**: `POST`  
**Authentication**: Required (JWT Bearer Token)  
**Content-Type**: `multipart/form-data`

**Request Body** (Form Data):
- `file` (MultipartFile, required): Image file (max 10MB)

**Response** (Success - 200):
```json
{
  "statusCode": "UAS",
  "message": "User Avatar updated successfully",
  "data": "http://localhost:8080/images/updated_avatar.jpg",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## 2. Post Management APIs

### 2.1 Create Post

**Endpoint**: `/api/v1/posts/create-post`  
**Method**: `POST`  
**Authentication**: Required (JWT Bearer Token)  
**Content-Type**: `multipart/form-data`

**Request Body** (Form Data):
- `postData` (JSON string, required): Post information
  ```json
  {
    "caption": "string (required)",
    "imageUrl": "string (optional)",
    "location": "string (optional)"
  }
  ```
- `file` (MultipartFile, required): Image file (max 10MB)

**Example Request**:
```
POST /api/v1/posts/create-post
Content-Type: multipart/form-data

postData: {"caption": "My first post", "location": "New York"}
file: [binary image data]
```

**Response** (Success - 200):
```json
{
  "statusCode": "PCS",
  "message": "Post Created Successfully.",
  "data": {
    "id": 1,
    "caption": "My first post",
    "imageUrl": "http://localhost:8080/images/post_image.jpg",
    "location": "New York",
    "createdAt": "2024-01-01T12:00:00",
    "username": "johndoe",
    "userAvatar": "http://localhost:8080/images/avatar.jpg",
    "likeCount": 0,
    "commentCount": 0,
    "isLikedByCurrentUser": false
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 2.2 Get All Posts

**Endpoint**: `/api/v1/posts/get-all-posts`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "PRS",
  "message": "Posts retrieved successfully.",
  "data": [
    {
      "id": 1,
      "caption": "Post caption",
      "imageUrl": "http://localhost:8080/images/post.jpg",
      "location": "New York",
      "createdAt": "2024-01-01T10:00:00",
      "username": "johndoe",
      "userAvatar": "http://localhost:8080/images/avatar.jpg",
      "likeCount": 25,
      "commentCount": 5,
      "isLikedByCurrentUser": true
    }
  ],
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 2.3 Get My Posts

**Endpoint**: `/api/v1/posts/get-my-posts`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "PRS",
  "message": "Posts retrieved successfully.",
  "data": [
    {
      "id": 1,
      "caption": "My post caption",
      "imageUrl": "http://localhost:8080/images/post.jpg",
      "location": "New York",
      "createdAt": "2024-01-01T10:00:00",
      "username": "johndoe",
      "userAvatar": "http://localhost:8080/images/avatar.jpg",
      "likeCount": 25,
      "commentCount": 5,
      "isLikedByCurrentUser": true
    }
  ],
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 2.4 Get Following Feed

**Endpoint**: `/api/v1/posts/feed`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Query Parameters**:
- `page` (integer, optional, default: 0): Page number for pagination
- `size` (integer, optional, default: 10): Number of items per page

**Example Request**:
```
GET /api/v1/posts/feed?page=0&size=10
```

**Response** (Success - 200):
```json
{
  "statusCode": "PRS",
  "message": "Posts retrieved successfully.",
  "data": {
    "content": [
      {
        "id": 1,
        "caption": "Post caption",
        "imageUrl": "http://localhost:8080/images/post.jpg",
        "location": "New York",
        "createdAt": "2024-01-01T10:00:00",
        "username": "johndoe",
        "userAvatar": "http://localhost:8080/images/avatar.jpg",
        "likeCount": 25,
        "commentCount": 5,
        "isLikedByCurrentUser": true
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 2.5 Edit Post

**Endpoint**: `/api/v1/posts/edit-post/{id}`  
**Method**: `PUT`  
**Authentication**: Required (JWT Bearer Token)  
**Content-Type**: `multipart/form-data`

**Path Parameters**:
- `id` (Long, required): Post ID

**Request Body** (Form Data):
- `postData` (JSON string, required): Updated post information
  ```json
  {
    "caption": "string (required)",
    "imageUrl": "string (optional)",
    "location": "string (optional)"
  }
  ```
- `file` (MultipartFile, optional): New image file (max 10MB)

**Example Request**:
```
PUT /api/v1/posts/edit-post/1
Content-Type: multipart/form-data

postData: {"caption": "Updated caption", "location": "Los Angeles"}
file: [binary image data] (optional)
```

**Response** (Success - 200):
```json
{
  "statusCode": "PUS",
  "message": "Post updated successfully.",
  "data": {
    "id": 1,
    "caption": "Updated caption",
    "imageUrl": "http://localhost:8080/images/updated_post.jpg",
    "location": "Los Angeles",
    "createdAt": "2024-01-01T10:00:00",
    "username": "johndoe",
    "userAvatar": "http://localhost:8080/images/avatar.jpg",
    "likeCount": 25,
    "commentCount": 5,
    "isLikedByCurrentUser": true
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 2.6 Delete Post

**Endpoint**: `/api/v1/posts/delete-post/{id}`  
**Method**: `DELETE`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `id` (Long, required): Post ID

**Example Request**:
```
DELETE /api/v1/posts/delete-post/1
```

**Response** (Success - 200):
```json
{
  "statusCode": "PDS",
  "message": "Post deleted successfully.",
  "data": "Post Deleted",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## 3. Comment Management APIs

### 3.1 Create Comment

**Endpoint**: `/api/v1/posts/{postId}/comment`  
**Method**: `POST`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `postId` (Long, required): Post ID

**Request Body**:
```json
{
  "content": "string (required)"
}
```

**Example Request**:
```
POST /api/v1/posts/1/comment
Content-Type: application/json

{
  "content": "This is a great post!"
}
```

**Response** (Success - 200):
```json
{
  "statusCode": "CCS",
  "message": "Comment created successfully",
  "data": {
    "id": 1,
    "content": "This is a great post!",
    "createdAt": "2024-01-01T12:00:00",
    "username": "johndoe",
    "userAvatar": "http://localhost:8080/images/avatar.jpg"
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 3.2 Get All Comments for a Post

**Endpoint**: `/api/v1/posts/{postId}/comments`  
**Method**: `GET`  
**Authentication**: Not Required

**Path Parameters**:
- `postId` (Long, required): Post ID

**Example Request**:
```
GET /api/v1/posts/1/comments
```

**Response** (Success - 200):
```json
{
  "statusCode": "CRS",
  "message": "Comment retrieved successfully",
  "data": [
    {
      "id": 1,
      "content": "This is a great post!",
      "createdAt": "2024-01-01T12:00:00",
      "username": "johndoe",
      "userAvatar": "http://localhost:8080/images/avatar.jpg"
    },
    {
      "id": 2,
      "content": "I agree!",
      "createdAt": "2024-01-01T12:05:00",
      "username": "janedoe",
      "userAvatar": "http://localhost:8080/images/avatar2.jpg"
    }
  ],
  "timeStamp": "2024-01-01T12:10:00"
}
```

---

### 3.3 Delete Comment

**Endpoint**: `/api/v1/posts/{postId}/comment/{commentId}/delete-comment`  
**Method**: `DELETE`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `postId` (Long, required): Post ID
- `commentId` (Long, required): Comment ID

**Example Request**:
```
DELETE /api/v1/posts/1/comment/1/delete-comment
```

**Response** (Success - 200):
```json
{
  "statusCode": "CDS",
  "message": "Comment deleted successfully",
  "data": "Comment Deleted",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## 4. Like Management APIs

### 4.1 Toggle Like on Post

**Endpoint**: `/api/v1/posts/{postId}/like`  
**Method**: `POST`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `postId` (Long, required): Post ID

**Example Request**:
```
POST /api/v1/posts/1/like
```

**Response** (Success - 200):
```json
{
  "statusCode": "LTS",
  "message": "Like a post successfully",
  "data": {
    "countLike": 25,
    "isLiked": true,
    "message": "Post liked" // or "Post unliked"
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## 5. Relationship Management APIs

### 5.1 Toggle Follow User

**Endpoint**: `/api/v1/follows/{targetUserId}/follow`  
**Method**: `POST`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `targetUserId` (Long, required): User ID to follow/unfollow

**Example Request**:
```
POST /api/v1/follows/2/follow
```

**Response** (Success - 200):
```json
{
  "statusCode": "FAS",
  "message": "Follow api success",
  "data": {
    "isFollowing": true,
    "isFriend": false,
    "message": "Successfully followed user", // or "Successfully unfollowed user"
    "followersCount": 51,
    "followingsCount": 31
  },
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## 6. Notification Management APIs

### 6.1 Get My Notifications

**Endpoint**: `/api/v1/notifications`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "NRS",
  "message": "Notification retrieved successfully",
  "data": [
    {
      "id": 1,
      "actorUsername": "janedoe",
      "actorAvatar": "http://localhost:8080/images/avatar2.jpg",
      "type": "LIKE", // or "COMMENT", "FOLLOW"
      "targetId": 1,
      "isRead": false,
      "createdAt": "2024-01-01T12:00:00"
    }
  ],
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 6.2 Get Unread Notification Count

**Endpoint**: `/api/v1/notifications/unread-count`  
**Method**: `GET`  
**Authentication**: Required (JWT Bearer Token)

**Response** (Success - 200):
```json
{
  "statusCode": "NCS",
  "message": "Notification count success",
  "data": 5,
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 6.3 Mark Notification as Read

**Endpoint**: `/api/v1/notifications/{notifyId}/read`  
**Method**: `PATCH`  
**Authentication**: Required (JWT Bearer Token)

**Path Parameters**:
- `notifyId` (Long, required): Notification ID

**Example Request**:
```
PATCH /api/v1/notifications/1/read
```

**Response** (Success - 200):
```json
{
  "statusCode": "NRS",
  "message": "Notification retrieved successfully",
  "data": "Notification marked as read",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

### 6.4 Mark All Notifications as Read

**Endpoint**: `/api/v1/notifications/read-all`  
**Method**: `PATCH`  
**Authentication**: Required (JWT Bearer Token)

**Example Request**:
```
PATCH /api/v1/notifications/read-all
```

**Response** (Success - 200):
```json
{
  "statusCode": "NRS",
  "message": "Notification retrieved successfully",
  "data": "All notifications marked as read",
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

### Public Endpoints (No Authentication Required):
- `POST /api/v1/user/register`
- `POST /api/v1/user/login`
- `GET /api/v1/posts/{postId}/comments`

---

## Error Responses

### 401 Unauthorized
```json
{
  "statusCode": "UIC",
  "message": "Invalid credentials",
  "data": "Authentication failed",
  "timeStamp": "2024-01-01T12:00:00"
}
```

### 400 Bad Request
```json
{
  "statusCode": "string",
  "message": "Validation error message",
  "data": null,
  "timeStamp": "2024-01-01T12:00:00"
}
```

### 404 Not Found
```json
{
  "statusCode": "UNF",
  "message": "User not found",
  "data": null,
  "timeStamp": "2024-01-01T12:00:00"
}
```

---

## Status Codes Reference

| Code | Description |
|------|-------------|
| PCS | Post Created Successfully |
| PRS | Posts retrieved successfully |
| PUS | Post updated successfully |
| PDS | Post deleted successfully |
| CCS | Comment created successfully |
| CRS | Comment retrieved successfully |
| CDS | Comment deleted successfully |
| LTS | Like a post successfully |
| URS | User registered successfully |
| ULS | User login successfully |
| UIC | Invalid credentials |
| FAS | Follow api success |
| UFS | User found success |
| UNF | User not found |
| NRS | Notification retrieved successfully |
| NCS | Notification count success |
| NUS | Notification updated successfully |
| UAS | User Avatar updated successfully |

---

## File Upload Constraints

- **Max file size**: 10MB per file
- **Max request size**: 50MB (for multipart requests)
- **Supported formats**: Images (jpg, png, etc.)
- **Upload directory**: `uploads/images/`
- **Access URL**: `http://localhost:8080/images/{filename}`

---

## Notes

1. All timestamps are in ISO 8601 format (LocalDateTime)
2. JWT tokens should be obtained from the login endpoint
3. Pagination is available for the feed endpoint
4. File uploads use multipart/form-data content type
5. All string validations are enforced on the server side
6. The `isFollowing` field in user responses indicates if the authenticated user is following that user
7. The `isLikedByCurrentUser` field in post responses indicates if the authenticated user has liked that post
