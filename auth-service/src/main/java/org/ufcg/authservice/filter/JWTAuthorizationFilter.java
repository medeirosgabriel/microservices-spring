package org.ufcg.authservice.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = this.getAuth(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);

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