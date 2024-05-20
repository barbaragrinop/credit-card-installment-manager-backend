package com.ccmi.api.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.configuration.security.TokenService;
import com.ccmi.api.dto.AuthenticationDTO;
import com.ccmi.api.entity.User;



@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager _authenticationManager;

    @Autowired
    private TokenService _tokenService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO user) {
        var token = new UsernamePasswordAuthenticationToken(user.email(), user.password());

        var authentication = _authenticationManager.authenticate(token);
        User principal = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
            _tokenService.generateToken(principal)
        );
    }

    
}
