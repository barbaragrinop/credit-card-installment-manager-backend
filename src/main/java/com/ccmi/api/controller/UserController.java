package com.ccmi.api.controller;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.dto.UserDTO;
import com.ccmi.api.entity.User;
import com.ccmi.api.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService _userService;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userData) {
        boolean userExists = _userService.findUserByEmail(userData.getEmail());

        if (userExists) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        User userDTOToEntity = modelMapper.map(userData, User.class);

        userDTOToEntity.setPassword(_passwordEncoder.encode(userData.getPassword()));

        User createdUser = _userService.createUser(userDTOToEntity);

        if(createdUser == null) {
            return ResponseEntity.badRequest().body("User could not be created!");
        }
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(
                location).body(createdUser);
    }

}
