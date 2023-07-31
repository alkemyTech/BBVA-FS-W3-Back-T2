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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transaction API", description = "Endpoints for Transaction Management")
public class TransactionController {

    private DepositService depositService;
    private TransactionService transactionService;
    private JwtService jwtService;

    private boolean isAdmin(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    private boolean isTransactionOwner(Transaction transaction, String username) {
        String transactionOwner = String.valueOf(transaction.getAccount().getUser());

        return transactionOwner.equals(username);
    }

    @Operation(summary = "Send ARS", description = "Perform a transaction to send ARS.")
    @PostMapping("/sendArs")
    public ResponseEntity<?> transactionHandlersendArs(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transaction details", required = true) @RequestBody TransactionRequestDTO transactionInput,
            @RequestHeader("Authorization") String token){
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

    @Operation(summary = "Send USD", description = "Perform a transaction to send USD.")
    @PostMapping("/sendUsd")
    public ResponseEntity<?> transactionHandlerSendUsd(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transaction details", required = true) @RequestBody TransactionRequestDTO transactionInput,
            @RequestHeader("Authorization") String token) {
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

    @Operation(summary = "Update Transaction", description = "Update transaction details by providing the transaction ID.")
    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @Parameter(description = "Transaction ID") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated transaction details", required = true) @RequestBody @Valid UpdateTransactionRequest updateTransactionRequest,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.updateTransaction(user, id, updateTransactionRequest));
    }

    @Operation(summary = "Deposit", description = "Perform a deposit.")
    @PostMapping("/deposit")
    public DepositResponse deposit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Deposit details", required = true) @RequestBody @Valid DepositRequest depositRequest,
            Authentication authentication){
        return (depositService.deposit(depositRequest, authentication));
    }

    @Operation(summary = "Get Transactions by User ID", description = "Get transactions of a user by their ID.")
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<?> getTransactionsById(
            @Parameter(description = "User ID") @PathVariable Long userId,
            Authentication authentication){
        User user = (User) authentication.getPrincipal();
        try {
            return new ResponseEntity<>(this.transactionService.getTransactionsById(userId, user.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Pay", description = "Perform a payment.")
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> pay(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment details", required = true) @RequestBody @Valid PaymentRequest paymentRequest,
            Authentication authentication)  {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.pay(user, paymentRequest));
    }

    @Operation(summary = "Get Transaction Detail", description = "Get transaction details by providing the transaction ID.")
    @GetMapping("/{id}")
    public Transaction getTransactionDetail(
            @Parameter(description = "Transaction ID") @PathVariable Long id) {
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

    @Operation(summary = "Get All Transactions", description = "Get a list of all transactions.")
    @GetMapping("")
    public PageTransactionResponse findAllUsers(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page) {
        try {
            return transactionService.findAllTransaction(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");
        }
    }
}