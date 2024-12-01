package com.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.apigateway.entity.User;
import com.apigateway.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	public UserRepository userRepository;
	
	@Cacheable(value="userByEmail", key="#email")
	public User getUserByEmail(String email) {
		
		return userRepository.findByEmail(email).orElse(null);
	}

}
