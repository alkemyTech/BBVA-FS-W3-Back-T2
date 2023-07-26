package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.exceptions.TransactionNotFoundException;
import com.bbva.wallet.services.DepositService;
import com.bbva.wallet.services.JwtService;
import com.bbva.wallet.services.TransactionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private DepositService depositService;
    private TransactionService transactionService;
    private JwtService jwtService;

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

    @PostMapping("/sendArs")
    public ResponseEntity<?> transactionHandlersendArs(@RequestBody TransactionRequestDTO transactionInput, @RequestHeader("Authorization") String token) {
        try {
            var jwt = token.substring(7);
            var userEmail = jwtService.extractUserName(jwt);
            Transaction transaction = transactionService.sendArs(userEmail, transactionInput);
            return new ResponseEntity<>(new TransactionResponseDTO(transaction), HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            return new ResponseEntity<>("Invalid token signature", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            return new ResponseEntity<>("Malformed token", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            return new ResponseEntity<>("Unsupported token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Illegal argument", HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/sendUsd")
    public ResponseEntity<?> transactionHandlerSendUsd(@RequestBody TransactionRequestDTO transactionInput, @RequestHeader("Authorization") String token) {
        try {
            var jwt = token.substring(7);
            var userEmail = jwtService.extractUserName(jwt);
            Transaction transaction = transactionService.sendUsd(userEmail, transactionInput);
            return new ResponseEntity<>(new TransactionResponseDTO(transaction), HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            return new ResponseEntity<>("Invalid token signature", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            return new ResponseEntity<>("Malformed token", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            return new ResponseEntity<>("Unsupported token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Illegal argument", HttpStatus.UNAUTHORIZED);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody @Valid UpdateTransactionRequest updateTransactionRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.updateTransaction(user, id, updateTransactionRequest));
    }

    @PostMapping("/deposit")
    public DepositResponse deposit(@RequestBody @Valid DepositRequest depositRequest, Authentication authentication) {
        return (depositService.deposit(depositRequest, authentication));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<?> getTransactionsById(@PathVariable Long userId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            return new ResponseEntity<>(this.transactionService.getTransactionsById(userId, user.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid PaymentRequest paymentRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.pay(user, paymentRequest));
    }

    @GetMapping("/{id}")
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

    @GetMapping("")
    public PageTransactionResponse findAllUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            return transactionService.findAllTransaction(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }
}