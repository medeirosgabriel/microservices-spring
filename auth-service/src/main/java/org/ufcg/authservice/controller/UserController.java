package org.ufcg.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ufcg.authservice.model.User;
import org.ufcg.authservice.service.UserService;

@RestController
@RequestMapping(value = "api/auth/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User findBYEmail(@RequestParam String email) {
        return this.userService.findByEmail(email);
    }
}
