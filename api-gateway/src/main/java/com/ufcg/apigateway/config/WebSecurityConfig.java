package com.ufcg.apigateway.config;

import com.ufcg.apigateway.filter.JWTAuthorizationFilter;
import com.ufcg.apigateway.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);

        authenticationManager.setPasswordEncoder(passwordEncoder());

        return authenticationManager;
    }

    // SecurityWebFilterChain for reactive module
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/auth/**").permitAll()
                .pathMatchers("/api/user/**").permitAll()
                .pathMatchers("/api/product/**").hasAuthority("ROLE_ADMIN")
                .anyExchange().authenticated()
        )
        .authenticationManager(authenticationManager())
        .addFilterBefore(new JWTAuthorizationFilter(), SecurityWebFiltersOrder.HTTP_BASIC);

        http.csrf().disable();

        return http.build();
    }
}
