package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.PageAccountResponse;
import com.bbva.wallet.dtos.UpdateAccountRequest;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.dtos.AccountCreationRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import com.bbva.wallet.dtos.AccountsBalanceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account API", description = "Endpoints for User Management")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get accounts by user ID", description = "Retrieve a list of accounts associated with a user by providing the user ID")
    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id || hasAuthority('ADMIN')")
    public List<Account> findAccountsByUser(@Parameter(description = "User ID") @PathVariable Long userId) {
        return accountService.findAccountsByUser(userId);
    }


    @Operation(summary = "Update account", description = "Update account details by providing the account ID")
    @PatchMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated account details", required = true) @RequestBody @Valid UpdateAccountRequest updateAccountRequest,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.updateAccount(user, accountId, updateAccountRequest));
    }


    @Operation(summary = "Create account", description = "Create a new account for the authenticated user")
    @PostMapping
    public ResponseEntity<Account> createAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account creation request", required = true) @RequestBody AccountCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.createAccount((User) authentication.getPrincipal(), request.getCurrency());
        return ResponseEntity
                .created(URI.create("/accounts/" + account.getCbu()))
                .body(account);
    }


    @Operation(summary = "Get accounts balance", description = "Retrieve the balance of all accounts associated with the authenticated user")
    @GetMapping("/balance")
    public AccountsBalanceResponse getBalance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return accountService.getAccountsBalance(user);
    }


    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all accounts")
    @GetMapping("")
    public PageAccountResponse findAllAccounts(@Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page) {
        try {
            return accountService.findAllAccounts(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }

}
