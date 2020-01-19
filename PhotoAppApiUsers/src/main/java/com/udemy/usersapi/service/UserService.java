package com.udemy.usersapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.udemy.usersapi.model.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto user);

	UserDto findUserByEmail(String email);

}
