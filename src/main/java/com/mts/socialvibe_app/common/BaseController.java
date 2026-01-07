package com.mts.socialvibe_app.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class BaseController {

    protected ResponseWrapper createResponse(MessageCode messageCode,Object data){
        return ResponseWrapper.from(messageCode.getStatusCode(),messageCode.getMessage(),data,LocalDateTime.now());
    }
}
