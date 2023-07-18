package com.bbva.wallet.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountBalanceDto {
    private String cbu;
    private Double balance;
}
