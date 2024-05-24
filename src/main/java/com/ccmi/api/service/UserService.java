package com.ccmi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ccmi.api.entity.User;
import com.ccmi.api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;

    public UserService(UserRepository userRepository) { }

    public User createUser(User entity) {
        return _userRepository.save(entity);
    }

    public boolean findUserByEmail(String email) {
        var user = _userRepository.findByEmail(email);
        
        if(user == null) {
            return false;
        }

        return true;
    }

}
