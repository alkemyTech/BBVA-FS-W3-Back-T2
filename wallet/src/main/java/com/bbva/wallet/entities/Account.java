package com.bbva.wallet.entities;
import com.bbva.wallet.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "soft_delete = false")
@Table(name = "accounts")
public class Account {

    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;

    @NotNull
    @Positive
    private Double transactionLimit;

    @NotNull
    private Double balance;

    @Id
    @Column(unique = true, nullable = false)
    private String cbu;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime creationDate;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @JsonIgnore
    private boolean softDelete;

}
