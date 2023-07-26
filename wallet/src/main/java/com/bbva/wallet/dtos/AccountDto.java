package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class AccountDto {

    private Currency currency;

    User user;

    String cbu;

    @CreationTimestamp
    LocalDateTime creationDate;

    public AccountDto(Account account) {
        this.currency = account.getCurrency();
        this.user = account.getUser();
        this.cbu = account.getCbu();
    }

}
