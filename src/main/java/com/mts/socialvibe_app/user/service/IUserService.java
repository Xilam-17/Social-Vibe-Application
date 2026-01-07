package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.user.dto.UserRequest;
import com.mts.socialvibe_app.user.dto.UserResponse;

public interface IUserService {
    UserResponse register(UserRequest userRequest);

    String verify(UserRequest userRequest);
}
