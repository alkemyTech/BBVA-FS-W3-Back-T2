package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.dtos.SignInRequest;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) throws AuthenticationException {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

}
