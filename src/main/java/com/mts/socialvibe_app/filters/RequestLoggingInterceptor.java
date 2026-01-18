package com.mts.socialvibe_app.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID_HEADER, requestId);
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        response.setHeader(REQUEST_ID_HEADER, requestId);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("=== API Request Started ===");
        log.info("Request ID: {}", requestId);
        log.info("HTTP Method: {}", method);
        log.info("Endpoint: {}", fullUrl);
        log.info("Client IP: {}", clientIp);
        log.info("User-Agent: {}", userAgent);
        
        // Log authentication info if available
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("Authentication: Bearer token present");
        } else {
            log.info("Authentication: No token");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        String requestId = (String) request.getAttribute(REQUEST_ID_HEADER);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        int statusCode = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (ex != null) {
            log.error("=== API Request Failed ===");
            log.error("Request ID: {}", requestId);
            log.error("HTTP Method: {}", method);
            log.error("Endpoint: {}", uri);
            log.error("Status Code: {}", statusCode);
            log.error("Duration: {} ms", duration);
            log.error("Error: {}", ex.getMessage(), ex);
        } else {
            log.info("=== API Request Completed ===");
            log.info("Request ID: {}", requestId);
            log.info("HTTP Method: {}", method);
            log.info("Endpoint: {}", uri);
            log.info("Status Code: {}", statusCode);
            log.info("Duration: {} ms", duration);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
