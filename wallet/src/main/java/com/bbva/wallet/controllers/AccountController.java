package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}