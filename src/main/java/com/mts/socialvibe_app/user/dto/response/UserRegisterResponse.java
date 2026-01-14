package com.mts.socialvibe_app.user.dto.response;

import com.mts.socialvibe_app.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;

    public static UserRegisterResponse mapToDto(UserEntity user) {
        return UserRegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
