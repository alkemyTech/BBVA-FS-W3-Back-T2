package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUser {
    private String firstName;
    private String lastName;
    private String password;
}
