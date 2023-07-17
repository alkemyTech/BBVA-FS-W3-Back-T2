package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.PageAccountResponse;
import com.bbva.wallet.dtos.UpdateAccount;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.dtos.AccountCreationRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import com.bbva.wallet.dtos.AccountsBalance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountId, @RequestBody @Valid UpdateAccount updateAccount, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.updateAccount(user, accountId, updateAccount));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Account account = accountService.createAccount((User) authentication.getPrincipal(), request.getCurrency());

        return ResponseEntity
                .created(URI.create("/accounts/" + account.getCbu()))
                .body(account);
    }

    @GetMapping("/balance")
    public AccountsBalance getBalance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return accountService.getAccountsBalance(user);
    }

    @GetMapping("")
    public PageAccountResponse findAllAccounts(@RequestParam(defaultValue = "0") int page) {
        try {
            return accountService.findAllAccounts(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }

}


