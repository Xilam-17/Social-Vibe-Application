package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.filters.jwt.JwtService;
import com.mts.socialvibe_app.user.dto.UserDto;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    private UserEntity mapToEntity(UserDto dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());
        return user;
    }

    private UserDto mapToDto(UserEntity user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPassword(null);
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        return dto;
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username '" + userDto.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email '" + userDto.getEmail() + "' is already registered.");
        }

        UserEntity user = mapToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public String verify(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userDto.getUsername());
        }

        return "Fail";
    }
}