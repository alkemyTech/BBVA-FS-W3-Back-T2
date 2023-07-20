package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UpdateUserRequest;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<User> findAllUsers() {
        return userService.findAllUsers();
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
