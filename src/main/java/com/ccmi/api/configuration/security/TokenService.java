package com.ccmi.api.configuration.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ccmi.api.entity.User;

@Service
public class TokenService {
    
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(
                "123456789"
            );
            return JWT.create()
                .withIssuer("API CCMI")
                .withSubject(user.getEmail())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
        } catch (JWTCreationException ex){
            throw new RuntimeException("Error creating token.", ex);
        }
    }

    public Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
