package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.PagedUserResponse;
import com.bbva.wallet.dtos.UpdateUserRequest;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    @PatchMapping("{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
