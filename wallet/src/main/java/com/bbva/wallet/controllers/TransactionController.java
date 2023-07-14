package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.exceptions.TransactionNotFoundException;
import com.bbva.wallet.services.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collection;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private boolean isAdmin(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        return isAdmin;
    }

    private boolean isTransactionOwner(Transaction transaction, String username) {
        String transactionOwner = String.valueOf(transaction.getAccount().getUser());

        boolean isOwner = transactionOwner.equals(username);

        return isOwner;
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionDetail(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Transaction transaction = transactionService.getTransactionById(id);

        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction not found");
        }

        if (!isAdmin(authentication) && !isTransactionOwner(transaction, username)) {
            throw new TransactionNotFoundException("Access denied");
        }

        return transaction;

    }
}

