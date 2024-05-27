package com.ccmi.api.configuration.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ccmi.api.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService _tokenService;

    @Autowired
    private UserRepository _userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = extractToken(request);

        if (token != null) { // If the token is not null
            var subject = _tokenService.getSubject(token); // Get the subject from the token

            var user = _userRepository.findByEmail(subject); // Find the user by email

            if (user != null) { // Check if the user is found
                // Create an authentication token
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Set the authentication in the context to force authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        var tokenFromHeader = request.getHeader("Authorization");

        if (tokenFromHeader != null) {
            return tokenFromHeader.replace("Bearer ", "");
        }

        return null;
    }
}