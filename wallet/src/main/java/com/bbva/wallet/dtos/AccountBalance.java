package com.bbva.wallet.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountBalance {
    private String cbu;
    private Double balance;
}
