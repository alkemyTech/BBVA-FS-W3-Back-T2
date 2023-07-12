package com.bbva.wallet.entities;

import com.bbva.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType name;

    String description;

    @CreationTimestamp
    LocalDateTime creationDate;

    @UpdateTimestamp
    LocalDateTime updatedDate;

    // no deberia estar las 2 cuentas en la clase, voy a suponer que esta es la cuenta receptora
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

