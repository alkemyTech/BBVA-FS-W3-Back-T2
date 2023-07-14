package com.bbva.wallet.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
@Entity
@Table(name = "fixed_term_deposits")
public class FixedTermDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "interest", nullable = false)
    private Double interest;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "closing_date")
    private Date closingDate;

}
