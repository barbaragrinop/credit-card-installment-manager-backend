package com.ccmi.api.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.configuration.security.TokenJWT;
import com.ccmi.api.configuration.security.TokenService;
import com.ccmi.api.dto.*;
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
        var authToken = new UsernamePasswordAuthenticationToken(user.email(), user.password());

        var authentication = _authenticationManager.authenticate(authToken);
        var tokenJWT = _tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }


}
