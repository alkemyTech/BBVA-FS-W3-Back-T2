package com.bbva.wallet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_config")
public class LoanConfig {

    @Id
    private Long id;

    @Column(name = "loan_interest")
    private double loanInterest = 0.05;
}
