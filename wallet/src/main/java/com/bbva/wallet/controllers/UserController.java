package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.PagedUserResponse;
import com.bbva.wallet.dtos.UpdateUserRequest;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Endpoints for User Management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Soft delete user", description = "Soft delete a user by providing the user ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAuthority('ADMIN')")
    public User userSoftDelete(
            @Parameter(description = "User ID") @PathVariable Long id) {
        return userService.userSoftDelete(id);
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public PagedUserResponse findAllUsers(@Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page) {
        try {
            return userService.findAllUsers(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }

    @Operation(summary = "Update user", description = "Update user details by providing the user ID")
    @PatchMapping("{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<User> updateUser(@Parameter(description = "User ID") @PathVariable Long id,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user details", required = true) @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @Operation(summary = "Find user by ID", description = "Retrieve user details by providing the user ID")
    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<User> findUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
