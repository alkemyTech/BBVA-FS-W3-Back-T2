package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //hql
    //@Query(value = "SELECT T FROM Transaction as T INNER JOIN Account as a ON T.account = a.cbu INNER JOIN User as u ON a.user.id = u.id WHERE u.id =? 1")
    //List<Transaction> getTransactionsById(Long userId);

    //native query
    @Query(value = "SELECT t.* FROM `transactions` t INNER JOIN accounts ON t.account_id = accounts.cbu WHERE accounts.user_id = ?1", nativeQuery = true)
    List<Transaction> getTransactionsById(Long userId);

}
