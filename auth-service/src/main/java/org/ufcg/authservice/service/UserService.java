package org.ufcg.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.ufcg.authservice.model.User;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public User findByEmail(String email) {
       User user = webClientBuilder.build().get()
                .uri("http://user-service/api/user/search",
                        uriBuilder -> uriBuilder.queryParam("email", email).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
       if (user == null) {
           throw new IllegalArgumentException("Email not found");
       }
       return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.findByEmail(username);
            System.out.println(user.getUsername() + " - " + user.getPassword());
            String password =  (new BCryptPasswordEncoder()).encode(user.getPassword());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), password, user.getAuthorities());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Email not Found");
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Email not Found");
        }
    }
}
