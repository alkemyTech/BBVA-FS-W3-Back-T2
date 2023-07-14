package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.PagedUserResponse;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAuthority('ADMIN')")
    public User userSoftDelete(@PathVariable Long id) {
        return userService.userSoftDelete(id);
    }

    @GetMapping
    public PagedUserResponse findAllUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            return userService.findAllUsers(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }

}
