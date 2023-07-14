package com.bbva.wallet.entities;

import com.bbva.wallet.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime creationDate;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedDate;


    @JsonIgnore
    // no deberia estar las 2 cuentas en la clase, voy a suponer que esta es la cuenta receptora
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

