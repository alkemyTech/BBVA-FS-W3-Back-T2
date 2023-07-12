package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long id;

    double amount;

    TransactionType name;

    String description;

    @CreationTimestamp
    LocalDateTime creationDate;

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.name = transaction.getName();
        this.description = transaction.getDescription();
        this.creationDate = transaction.getCreationDate();
    }

}
