package com.example.userservice.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.userservice.domain.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByUserId(String userId);

	UserEntity findByEmail(String username);
}
