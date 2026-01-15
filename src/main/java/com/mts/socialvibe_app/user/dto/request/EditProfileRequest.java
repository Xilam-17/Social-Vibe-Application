package com.mts.socialvibe_app.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditProfileRequest {
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
