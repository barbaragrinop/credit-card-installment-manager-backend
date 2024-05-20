package com.ccmi.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.dto.UserDTO;
import com.ccmi.api.entity.User;
import com.ccmi.api.service.UserService;


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


        return ResponseEntity.ok(_userService.createUser(userDTOToEntity));        
    }
}
