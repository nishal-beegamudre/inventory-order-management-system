package com.apigateway.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.apigateway.repository.UserRepository;
import com.apigateway.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class UserDetailsConfig {
	
	@Autowired
	public UserRepository userRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsServiceImpl userDetailsService(UserRepository userRepo) {
    	
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	
    	List<com.apigateway.entity.User> user = new ArrayList<com.apigateway.entity.User>();
    	
    	if(userRepo==null) {
    		user = userRepository.findAll();
    	}else {
    		user = userRepo.findAll();
    	}
    	
    	
    	Collection<UserDetails> userDetailsCollection = new ArrayList<UserDetails>();
    	
    	user.forEach(userVar -> {
    		
    		UserDetails userDetails = User.builder()
    	            .username(userVar.getEmail())
    	            .password(userVar.getPassword())
    	            .roles("USER")
    	            .build();
    		
    		userDetailsCollection.add(userDetails);
    	});
    	
        UserDetails userDefault = User.builder()
                .username("adamk")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        
        userDetailsCollection.add(userDefault);
    	
        return new MapReactiveUserDetailsServiceImpl(userDetailsCollection);
    	
    }

}
