package com.bbva.wallet.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanSimulateResponse {
    @JsonProperty("CoutasAPagar")
    private double monthlyPayments;

    @JsonProperty("TotalAPagar")
    private double totalToPay;

    @JsonProperty("InteresMensual")
    private double interestRates;

}
