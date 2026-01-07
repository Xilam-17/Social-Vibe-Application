package com.mts.socialvibe_app.user.service;

import com.mts.socialvibe_app.filters.jwt.JwtService;
import com.mts.socialvibe_app.user.dto.UserRequest;
import com.mts.socialvibe_app.user.dto.UserResponse;
import com.mts.socialvibe_app.user.model.UserEntity;
import com.mts.socialvibe_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username '" + userRequest.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email '" + userRequest.getEmail() + "' is already registered.");
        }

        UserEntity user = UserEntity.mapToEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return UserResponse.mapToDto(savedUser);
    }

    @Override
    public String verify(UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userRequest.getUsername());
        }

        return "Fail";
    }
}