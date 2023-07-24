package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanSimulateRequest;
import com.bbva.wallet.dtos.LoanSimulateResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanService {
    public LoanSimulateResponse simulateLoan(LoanSimulateRequest loanSimulateRequest) {

        double loanInterest = 0.05;
        double interestRatesCalculate = loanInterest * loanSimulateRequest.getInstallments();
        double totalPayCalculate =loanSimulateRequest.getAmount()+(loanSimulateRequest.getAmount() * interestRatesCalculate);
        double monthlyPaymentsCalculate = totalPayCalculate / loanSimulateRequest.getInstallments();

        LoanSimulateResponse loanSimulateResponse = new LoanSimulateResponse();
        loanSimulateResponse.setInterestRates(interestRatesCalculate);
        loanSimulateResponse.setTotalToPay(totalPayCalculate);
        loanSimulateResponse.setMonthlyPayments(monthlyPaymentsCalculate);

        return loanSimulateResponse;
    }
}
