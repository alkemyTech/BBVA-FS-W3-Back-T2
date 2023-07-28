package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;

    double amount;

    TransactionType name;

    String description;

    @CreationTimestamp
    LocalDateTime creationDate;

    public TransactionResponseDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.name = transaction.getName();
        this.description = transaction.getDescription();
        this.creationDate = transaction.getCreationDate();
    }

}
