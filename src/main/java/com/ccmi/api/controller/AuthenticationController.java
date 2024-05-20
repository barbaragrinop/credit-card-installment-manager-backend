package com.ccmi.api.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.dto.AuthenticationDTO;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager _authenticationManager;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationDTO user) {
        var token = new UsernamePasswordAuthenticationToken(user.email(), user.senha());

        var authentication = _authenticationManager.authenticate(token);
        return ResponseEntity.ok("Hello World");
    }
}
