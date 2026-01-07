package com.mts.socialvibe_app.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper <T>{

    private String statusCode;
    private String message;
    private T data;
    private LocalDateTime timeStamp;

    public static  ResponseWrapper from(String statusCode, String message, Object data, LocalDateTime timeStamp) {
        return ResponseWrapper.builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timeStamp(timeStamp)
                .build();
    }
}
