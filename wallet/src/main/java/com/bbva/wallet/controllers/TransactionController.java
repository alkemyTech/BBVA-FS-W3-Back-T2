package com.bbva.wallet.controllers;


import com.bbva.wallet.dtos.TransactionInputDto;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.JwtUtil;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {


    private TransactionService transactionService;



    @PostMapping("/send_ars")
    public ResponseEntity<?> TransactionHandlerSendArs(@RequestBody TransactionInputDto transactionInput, @RequestHeader("Authorization") String token) {
        try {
           var claims =  JwtUtil.validateToken(token);
            transactionService.SendArs(claims.get("user_id",Long.class), transactionInput);
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
    public ResponseEntity<?> TransactionHandlerSendUsd(@RequestBody TransactionInputDto transactionInput, @RequestHeader("Authorization") String token) {
        try {
            var claims =  JwtUtil.validateToken(token);
            transactionService.SendUsd(claims.get("user_id",Long.class), transactionInput);
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


}
