package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountCreationRequest;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.services.AccountService;

import java.net.URI;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private  AccountService accountService;

    @PostMapping()
    public ResponseEntity<Account> createAccount(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody @Valid AccountCreationRequest request
    ) {
        Account account = accountService.createAccount(authenticatedUser, request);
        return ResponseEntity
                .created(URI.create("/accounts/" + account.getCbu()))
                .body(account);
    }

}

/*
    @GetMapping("/dni")
    public ResponseEntity<Account> getById(Authentication user) {
        Account account = (Account) user.getPrincipal();
        Optional<Account> entity = accountService.getByUserId(account.getUserId());
        return entity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
