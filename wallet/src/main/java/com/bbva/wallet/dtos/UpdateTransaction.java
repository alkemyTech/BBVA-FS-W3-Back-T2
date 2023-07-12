package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTransaction {
    @NotBlank(message = "El campo de descripción no puede estar vacío")
    String description;
}
