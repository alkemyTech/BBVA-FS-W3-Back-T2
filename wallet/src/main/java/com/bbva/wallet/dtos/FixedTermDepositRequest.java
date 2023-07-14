package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FixedTermDepositRequest {

    @NotNull
    @Min(500)
    private Double amount;

    @NotNull
    @Min(30)
    private int totalDays;

}