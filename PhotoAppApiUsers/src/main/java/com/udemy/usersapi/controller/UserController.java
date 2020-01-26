package com.udemy.usersapi.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.usersapi.model.UserDto;
import com.udemy.usersapi.model.UserRequest;
import com.udemy.usersapi.model.UserResponse;
import com.udemy.usersapi.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment environment;

	@Autowired
	UserService userService;

	@GetMapping("/status/check")
	public String status() {
		return "worked on port:" + environment.getProperty("local.server.port") + "with secret"
				+ environment.getProperty("token.secret");
	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
		UserDto userDto = new ModelMapper().map(userRequest, UserDto.class);
		UserResponse response = new ModelMapper().map(userService.createUser(userDto), UserResponse.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(value = "/{userId}")
	public ResponseEntity<?> getUser(@PathVariable String userId) {
		UserDto userDto = userService.getUserByUserId(userId);
		UserResponse response = new ModelMapper().map(userDto, UserResponse.class);
		return ResponseEntity.status(HttpStatus.FOUND).body(response);
	}

}
