package com.ccmi.api.repository;

import org.springframework.stereotype.Repository;

import com.ccmi.api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{ 

    UserDetails findByEmail(String username);
}


