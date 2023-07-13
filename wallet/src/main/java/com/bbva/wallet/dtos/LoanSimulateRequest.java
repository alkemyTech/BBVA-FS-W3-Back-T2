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

    @Min(value = 1, message ="El plazo de un prestamo debe ser de 1 a 36 meses" )
    @Max(value = 36, message ="El plazo de un prestamo debe ser de 1 a 36 meses")
    private int deadlines;

    @Min(value = 1)
    private double amount;



}
