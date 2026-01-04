package com.mts.socialvibe_app.filters.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String extractName(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
