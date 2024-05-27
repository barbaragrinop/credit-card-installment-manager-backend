package com.ccmi.api.configuration.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ccmi.api.entity.User;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(
                    secret);
            return JWT.create()
                    .withIssuer("API CCMI")
                    .withClaim("id", user.getId())
                    .withClaim("name", user.getName())
                    .withSubject( user.getEmail())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error creating token.", ex);
        }
    }

    
    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API CCMI")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Token invalid or expired .", ex);
        }
    }

    public Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
