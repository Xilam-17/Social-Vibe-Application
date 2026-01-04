package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.user.dto.UserDto;

public interface IUserService {
    UserDto register(UserDto userDto);

    String verify(UserDto userDto);
}
