package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanSimulateRequest;
import com.bbva.wallet.dtos.LoanSimulateResponse;
import com.bbva.wallet.entities.LoanConfig;
import com.bbva.wallet.repositories.LoanConfigRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanService {
    private LoanConfigRepository loanConfigRepository;

    public LoanSimulateResponse simulateLoan(LoanSimulateRequest loanSimulateRequest) {
        var loanConfig = loanConfigRepository.findById(1L).get();
        double interestRatesCalculate = (loanConfig.getLoanInterest() * loanSimulateRequest.getDeadlines()) / 12;
        double totalPayCalculate =loanSimulateRequest.getAmount()+((loanSimulateRequest.getAmount() * interestRatesCalculate) / 100);
        double monthlyPaymentsCalculate = totalPayCalculate / loanSimulateRequest.getDeadlines();


        LoanSimulateResponse loanSimulateResponse = new LoanSimulateResponse();
        loanSimulateResponse.setInterestRates(interestRatesCalculate);
        loanSimulateResponse.setTotalToPay(totalPayCalculate);
        loanSimulateResponse.setMonthlyPayments(monthlyPaymentsCalculate);


        return loanSimulateResponse;
    }
}