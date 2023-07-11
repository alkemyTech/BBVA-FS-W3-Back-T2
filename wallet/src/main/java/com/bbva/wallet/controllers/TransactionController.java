package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.Payment;
import com.bbva.wallet.dtos.PaymentRegister;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/payment")
    public ResponseEntity<PaymentRegister> pay(@RequestBody @Valid Payment payment, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.pay(user, payment));
    }
}
