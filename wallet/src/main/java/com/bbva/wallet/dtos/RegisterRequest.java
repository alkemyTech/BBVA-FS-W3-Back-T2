package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "El campo del nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El campo del apellido no puede estar vacío")
    private String lastName;

    @NotNull(message = "El campo de la edad no puede estar vacío")
    @Min(value = 18, message = "La edad mínima para ingresar es 18 años")
    private Integer age;

    @NotBlank(message = "El campo de la contraseña no puede estar vacío")
    private String password;

    @NotBlank(message = "El campo del correo electrónico no puede estar vacío")
    @Email(message = "El formato del correo electrónico es incorrecto")
    private String email;
}