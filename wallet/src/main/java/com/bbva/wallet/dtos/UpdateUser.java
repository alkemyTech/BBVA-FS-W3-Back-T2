package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUser {
    @NotBlank(message = "El campo del nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El campo del apellido no puede estar vacío")
    private String lastName;

    @NotBlank(message = "El campo de la contraseña no puede estar vacío")
    private String password;
}
