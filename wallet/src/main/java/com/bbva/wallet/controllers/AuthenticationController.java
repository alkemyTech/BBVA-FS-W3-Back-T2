package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.dtos.SignInRequest;
import com.bbva.wallet.dtos.SignInResponse;
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
    public ResponseEntity<SignInResponse> signup(@RequestBody @Valid RegisterRequest request) {
        SignInResponse signInResponse = authenticationService.signUp(request);

        return ResponseEntity
                .created(URI.create("/users" + signInResponse.getUser().getId()))
                .body(signInResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) throws AuthenticationException {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

}
