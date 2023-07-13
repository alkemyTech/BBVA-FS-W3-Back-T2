package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanSimulateRequest;
import com.bbva.wallet.dtos.LoanSimulateResponse;
import com.bbva.wallet.services.LoanService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@AllArgsConstructor
public class LoanController {

    private LoanService loanService;

    @PostMapping("/simulate")
    public ResponseEntity<LoanSimulateResponse> simulateLoan(@RequestBody @Valid LoanSimulateRequest loanSimulateRequest) {
        return new ResponseEntity<>(this.loanService.simulateLoan(loanSimulateRequest), HttpStatus.OK);
    }

}
