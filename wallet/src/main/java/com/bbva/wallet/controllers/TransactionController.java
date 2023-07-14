package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.Payment;
import com.bbva.wallet.dtos.PaymentRegister;
import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.services.DepositService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bbva.wallet.dtos.DepositRequest;
import com.bbva.wallet.dtos.DepositResponse;
import com.bbva.wallet.dtos.TransactionInputDto;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.services.JwtService;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private TransactionService transactionService;
    private JwtService jwtService;
    private DepositService depositService;

    @PostMapping("/send_ars")
    public ResponseEntity<?> transactionHandlersendArs(@RequestBody TransactionInputDto transactionInput, @RequestHeader("Authorization") String token) {
        try {
            var jwt = token.substring(7);
             var userEmail = jwtService.extractUserName(jwt);
            transactionService.sendArs(userEmail,transactionInput);
            return  new ResponseEntity<>("Transaccion exitosa", HttpStatus.OK);
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

    @PostMapping("/send_usd")
    public ResponseEntity<?> transactionHandlerSendUsd(@RequestBody TransactionInputDto transactionInput, @RequestHeader("Authorization") String token) {
        try {
            var jwt = token.substring(7);
            var userEmail = jwtService.extractUserName(jwt);
            transactionService.sendUsd(userEmail,transactionInput);
            return  new ResponseEntity<>("Transaccion exitosa", HttpStatus.OK);
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

    @PostMapping("/deposit")
    public DepositResponse deposit(@RequestBody @Valid DepositRequest depositRequest, Authentication authentication){
        return (depositService.deposit(depositRequest, authentication));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<?>  getTransactionsById (@PathVariable Long userId, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        try {
            return new ResponseEntity<>(this.transactionService.getTransactionsById(userId, user.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentRegister> pay(@RequestBody @Valid Payment payment, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.pay(user, payment));
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
