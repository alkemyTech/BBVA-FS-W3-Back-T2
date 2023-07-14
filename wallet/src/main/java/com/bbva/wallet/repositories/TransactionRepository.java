package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
    @Query(value = "SELECT t.* FROM `transactions` t INNER JOIN accounts ON t.account_id = accounts.cbu WHERE accounts.user_id = ?1", nativeQuery = true)
    List<Transaction> getTransactionsById(Long userId);

    Page<Transaction> findAll(Pageable pageable);
}
