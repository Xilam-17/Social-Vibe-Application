package com.mts.socialvibe_app.user.controller;

import com.mts.socialvibe_app.filters.jwt.JwtService;
import com.mts.socialvibe_app.user.dto.UserDto;
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
public class UserController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(service.register(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        String token = service.verify(userDto);
        if(token.equals("Fail")) {
            return new ResponseEntity<>("Invalid Credentials!", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(token,HttpStatus.OK);

    }
}
