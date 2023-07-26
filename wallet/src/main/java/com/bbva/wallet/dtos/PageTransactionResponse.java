package com.bbva.wallet.dtos;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageTransactionResponse {
    private List<TransactionDto> transactions;
    private String previousPageUrl;
    private String nextPageUrl;
}
