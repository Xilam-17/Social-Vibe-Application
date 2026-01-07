package com.mts.socialvibe_app.user.controller;

import com.mts.socialvibe_app.common.BaseController;
import com.mts.socialvibe_app.common.MessageCode;
import com.mts.socialvibe_app.common.ResponseWrapper;
import com.mts.socialvibe_app.user.dto.UserRequest;
import com.mts.socialvibe_app.user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseWrapper register(@Valid @RequestBody UserRequest userRequest) {
        return createResponse(MessageCode.USER_REGISTER_SUCCESS, service.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseWrapper login(@RequestBody UserRequest userRequest) {
        try {
            String token = service.verify(userRequest);
            return createResponse(MessageCode.USER_LOGIN_SUCCESS, token);
        } catch (Exception e) {
            return createResponse(MessageCode.UNAUTHORIZED_INVALID_CREDENTIALS, "Invalid username or password");
        }
    }
}
