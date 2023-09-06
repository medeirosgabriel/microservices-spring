package org.ufcg.authservice.service;

import org.ufcg.authservice.model.User;
import org.ufcg.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return this.userRepository.findById(id).get();
    }
}
