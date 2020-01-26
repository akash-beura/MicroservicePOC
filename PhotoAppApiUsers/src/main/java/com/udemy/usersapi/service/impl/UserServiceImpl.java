package com.udemy.usersapi.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.udemy.usersapi.model.AlbumResponse;
import com.udemy.usersapi.model.User;
import com.udemy.usersapi.model.UserDto;
import com.udemy.usersapi.repository.UserRepository;
import com.udemy.usersapi.service.UserService;
import com.udemy.usersapi.service.feign.UserAlbumsClient;

@Service
public class UserServiceImpl implements UserService {

	private ModelMapper mapper;

	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder;

	private UserAlbumsClient albumbsClient;

	@Autowired
	public UserServiceImpl(ModelMapper mapper, UserRepository userRepository, BCryptPasswordEncoder encoder,
			UserAlbumsClient albumsClient) {
		this.mapper = mapper;
		this.userRepository = userRepository;
		this.passwordEncoder = encoder;
		this.albumbsClient = albumsClient;

	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getEncryptedPassword()));
		userDetails.setUserId(UUID.randomUUID().toString());
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		User user = mapper.map(userDetails, User.class);
		userRepository.save(user);
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(),
				true, true, true, true, Collections.emptyList());
	}

	@Override
	public UserDto findUserByEmail(String email) {
		User user = userRepository.findByEmail(email).get();
		return mapper.map(user, UserDto.class);
	}

	public UserDto getUserByUserId(String userId) {
		User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException());
		UserDto dto = mapper.map(user, UserDto.class);
		List<AlbumResponse> albums = albumbsClient.getAlbums(userId);
		dto.setAlbums(albums);
		return dto;
	}

}
