package com.apigateway.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.apigateway.repository.UserRepository;
import com.apigateway.service.UserService;

import reactor.core.publisher.Mono;

@Component @Primary
public class MapReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {
	
Logger logger = LoggerFactory.getLogger(MapReactiveUserDetailsServiceImpl.class);
	
	
	@Autowired
	public UserService userService;
	
	public Map<String, UserDetails> users = new HashMap<String, UserDetails>();

	/**
	 * Creates a new instance using a {@link Map} that must be non blocking.
	 * @param users a {@link Map} of users to use.
	 */
	public MapReactiveUserDetailsServiceImpl(Map<String, UserDetails> users) {
		this.users = users;
	}
	
	public MapReactiveUserDetailsServiceImpl() {
		
	}

	/**
	 * Creates a new instance
	 * @param users the {@link UserDetails} to use
	 */
	public MapReactiveUserDetailsServiceImpl(UserDetails... users) {
		this(Arrays.asList(users));
	}

	/**
	 * Creates a new instance
	 * @param users the {@link UserDetails} to use
	 */
	public MapReactiveUserDetailsServiceImpl(Collection<UserDetails> users) {
		//Assert.notEmpty(users, "users cannot be null or empty");
		this.users = new ConcurrentHashMap<>();
		for (UserDetails user : users) {
			this.users.put(getKey(user.getUsername()), user);
		}
	}
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		
		logger.info("Entered impl");
		
		com.apigateway.entity.User user = userService.getUserByEmail(username);
		
		if(user==null) {
			return Mono.empty();
		}
		else {
		UserDetails result = User.builder()
	            .username(user.getEmail())
	            .password(user.getPassword())
	            .roles("USER")
	            .build();
		
		logger.info("Build impl done");
				
		return (result != null) ? Mono.just(User.withUserDetails(result).build()) : Mono.empty();
		
		}
	}

	@Override
	public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
		// @formatter:off
		return Mono.just(user)
				.map((userDetails) -> withNewPassword(userDetails, newPassword))
				.doOnNext((userDetails) -> {
					String key = getKey(user.getUsername());
					this.users.put(key, userDetails);
				});
		// @formatter:on
	}

	private UserDetails withNewPassword(UserDetails userDetails, String newPassword) {
		// @formatter:off
		return User.withUserDetails(userDetails)
				.password(newPassword)
				.build();
		// @formatter:on
	}

	private String getKey(String username) {
		return username.toLowerCase();
	}

}
