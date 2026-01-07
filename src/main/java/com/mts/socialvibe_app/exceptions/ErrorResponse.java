package com.mts.socialvibe_app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public static ErrorResponse from(int status, String message) {
        ErrorResponse errorResponse =  new ErrorResponse();
        errorResponse.setStatus(status);
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(LocalDateTime.now());

        return errorResponse;
    }
}
