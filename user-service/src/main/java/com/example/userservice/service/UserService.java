package com.example.userservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.domain.UserEntity;
import com.example.userservice.domain.repository.UserRepository;
import com.example.userservice.dto.UserDto;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security User 관련 Service
 * security 의 UserDetailService 인터페이스 구현 필수
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final ModelMapper mapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(username);

		if (user == null)
			throw new UsernameNotFoundException(username);

		return new User(
			user.getEmail(),
			user.getEncryptedPwd(),
			true,
			true,
			true,
			true,
			new ArrayList<>());
	}

	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

		userRepository.save(userEntity);

		return mapper.map(userEntity, UserDto.class);
	}

	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User Not Found");
		}

		UserDto userDto = mapper.map(userEntity, UserDto.class);
		userDto.setOrders(Collections.emptyList());

		return userDto;
	}

	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}

	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return mapper.map(userEntity, UserDto.class);
	}
}
