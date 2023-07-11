
package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountCreationRequest;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{userId}")
    public List<Account> findAccountsByUser (@PathVariable Long userId){
        return accountService.findAccountsByUser(userId);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Account account = accountService.createAccount((User) authentication.getPrincipal(), request.getCurrency());

        return ResponseEntity
                .created(URI.create("/accounts/" + account.getCbu()))
                .body(account);
    }

}


