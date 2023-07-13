package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermRequest;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.services.FixedTermDepositService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FixedTermController {

    private final FixedTermDepositService fixedTermDepositService;

    @Autowired
    public FixedTermController(FixedTermDepositService fixedTermDepositService) {
        this.fixedTermDepositService = fixedTermDepositService;
    }

    @PostMapping("/fixedTerm/simulate")
    public FixedTermDeposit simulateFixedTermDeposit(@RequestBody @Valid FixedTermRequest fixedTermRequest, Authentication authentication) {
        return fixedTermDepositService.simulateFixedTermDeposit(fixedTermRequest, authentication);
    }

}