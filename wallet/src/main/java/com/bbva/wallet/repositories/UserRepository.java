package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}

