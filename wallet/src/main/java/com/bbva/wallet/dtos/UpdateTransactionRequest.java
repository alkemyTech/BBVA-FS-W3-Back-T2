package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTransactionRequest {
    @NotBlank(message = "El campo de descripción no puede estar vacío")
    String description;
}
