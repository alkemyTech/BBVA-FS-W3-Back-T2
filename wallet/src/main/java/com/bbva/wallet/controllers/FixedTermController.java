package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermDepositRequest;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.dtos.FixedTermDepositResponse;
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
    public FixedTermDepositResponse simulateFixedTermDeposit(@RequestBody @Valid FixedTermDepositRequest fixedTermRequest, Authentication authentication) {
        return fixedTermDepositService.simulateFixedTermDeposit(fixedTermRequest, authentication);
    }

    @PostMapping("/fixedTerm")
    public FixedTermDepositResponse createFixedTermDeposit(@RequestBody @Valid FixedTermDepositRequest fixedTermRequest, Authentication authentication) {
        return fixedTermDepositService.createFixedTermDeposit(fixedTermRequest, authentication);
    }

}