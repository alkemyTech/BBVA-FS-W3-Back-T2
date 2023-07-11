package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UpdateAccount;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

}
