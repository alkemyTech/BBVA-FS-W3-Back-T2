package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> signup(@RequestBody @Valid RegisterRequest request) {
        User user = authenticationService.signUp(request);
        return ResponseEntity
                .created(URI.create("/user/" + user.getId()))
                .body(user);
    }

}
