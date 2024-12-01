package com.apigateway.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.apigateway.service.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .cast(JwtToken.class)
                .filter(jwtToken -> jwtService.isTokenValid(jwtToken.getToken()))
                .map(jwtToken -> jwtToken.withAuthenticated(true))
                .switchIfEmpty(Mono.error(new JwtAuthenticationException("Invalid token.")));
    }
}
