package com.bbva.wallet.dtos;
import com.bbva.wallet.entities.User;
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




