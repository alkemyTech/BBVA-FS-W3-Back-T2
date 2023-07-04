package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Role, Long> {
}