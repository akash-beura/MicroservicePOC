package com.udemy.usersapi.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.usersapi.model.LoginRequest;
import com.udemy.usersapi.model.UserDto;
import com.udemy.usersapi.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Environment environment;
	private UserService userService;

	@Autowired
	public AuthenticationFilter(Environment environment, UserService userService, AuthenticationManager authManager) {

		super.setAuthenticationManager(authManager);
		this.environment = environment;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
					credentials.getEmail(), credentials.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException();
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String username = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
		UserDto userDto = userService.findUserByEmail(username);
		String token = "Bearer " + Jwts.builder().setSubject(userDto.getUserId())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"), 10)))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret")).compact();
		res.addHeader("token", token);
		res.addHeader("userId", userDto.getUserId());
	}

}
