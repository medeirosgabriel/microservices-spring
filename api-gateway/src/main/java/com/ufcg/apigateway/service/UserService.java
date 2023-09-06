package com.ufcg.apigateway.service;

import com.ufcg.apigateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        UserDetails user = webClientBuilder.build().get()
                .uri("http://user-service/api/user/search",
                        uriBuilder -> uriBuilder.queryParam("email", email).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
        if (user == null) {
            throw new IllegalArgumentException("Email not found");
        }
        return Mono.just(user);
    }
}
