package com.ufcg.apigateway.filter;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthorizationFilter  implements WebFilter {

    private ReactiveAuthenticationManager authManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String token = request.getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer")) {
            return chain.filter(exchange);
        }

        UsernamePasswordAuthenticationToken authentication = this.getAuth(token);

        return chain.filter(exchange)
                .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private UsernamePasswordAuthenticationToken getAuth(String token) {

        String email = Jwts.parser()
                .setSigningKey("mysecret")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();

        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) Jwts.parser()
                .setSigningKey("mysecret")
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody()
                .get("roles", List.class)
                .stream().map(r -> new SimpleGrantedAuthority((String) r)).collect(Collectors.toList());


        if (Objects.nonNull(email)) {
            return new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    roles);
        } else {
            return null;
        }

    }
}