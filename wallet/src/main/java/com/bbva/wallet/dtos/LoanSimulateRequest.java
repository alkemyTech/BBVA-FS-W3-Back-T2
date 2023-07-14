package com.bbva.wallet.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanSimulateRequest {

    @Min(value = 1, message ="El plazo de un prestamo debe ser al menos 1" )
    private int installments;

    @Min(value = 1)
    private double amount;



}
