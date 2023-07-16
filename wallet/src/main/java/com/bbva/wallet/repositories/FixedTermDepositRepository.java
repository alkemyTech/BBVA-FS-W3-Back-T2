package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixedTermDepositRepository  extends JpaRepository<FixedTermDeposit, Long> {

    List<FixedTermDeposit> findByAccount(Account account);
}
