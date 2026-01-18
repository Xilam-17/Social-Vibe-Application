package com.mts.socialvibe_app.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmFriendRequest {
    @NotNull(message = "Target user ID is required")
    private Long targetUserId;

    @NotNull(message = "Confirm status is required")
    private Boolean confirm;
}
