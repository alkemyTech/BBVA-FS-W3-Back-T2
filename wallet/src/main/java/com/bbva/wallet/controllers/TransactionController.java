package com.bbva.wallet.controllers;


import com.bbva.wallet.dtos.TransactionInputDto;
import com.bbva.wallet.dtos.UpdateTransaction;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bbva.wallet.services.JwtService;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {


    private TransactionService transactionService;
    private JwtService jwtService;


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

    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody @Valid UpdateTransaction updateTransaction, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.updateTransaction(user, id, updateTransaction));
    }


}
