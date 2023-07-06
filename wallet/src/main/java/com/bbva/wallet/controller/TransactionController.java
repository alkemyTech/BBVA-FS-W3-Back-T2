package com.bbva.wallet.controller;


import com.bbva.wallet.dto.TransactionInputDto;
import com.bbva.wallet.service.TransactionService;
import com.bbva.wallet.utils.JwtUtil;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {


    private TransactionService transactionService;



    @PostMapping
    public ResponseEntity<?> TransactionHandler(@RequestBody TransactionInputDto transactionInput, @RequestHeader("Authorization") String token) {
        try {
           var claims =  JwtUtil.validateToken(token);
            transactionService.SendArs(claims.get("user_id",Long.class), transactionInput);
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


        return null ;

    }


}
