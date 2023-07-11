package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.DepositRequest;
import com.bbva.wallet.dtos.DepositResponse;
import com.bbva.wallet.services.DepositService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private final DepositService depositService;

    public TransactionController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/deposit")
    public DepositResponse deposit(@RequestBody @Valid DepositRequest depositRequest, HttpServletRequest request){
        return (depositService.deposit(depositRequest, request));
    }

}
