package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAuthority('ADMIN')")
    public User userSoftDelete(@PathVariable Long id){

        return userService.userSoftDelete(id);

    }

}

