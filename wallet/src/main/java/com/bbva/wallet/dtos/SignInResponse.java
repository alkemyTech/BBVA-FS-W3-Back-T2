package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private User user;
    private String jwt;
}

