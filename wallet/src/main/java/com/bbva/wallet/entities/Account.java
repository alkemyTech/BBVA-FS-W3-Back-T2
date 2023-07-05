package com.bbva.wallet.entities;
import com.bbva.wallet.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    private boolean softDelete;

}
