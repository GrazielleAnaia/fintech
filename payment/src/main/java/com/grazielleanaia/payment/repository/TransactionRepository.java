package com.grazielleanaia.payment.repository;


import com.grazielleanaia.payment.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transactions, UUID> {

    Optional<Transactions> findByReferenceId(UUID referenceId);
}
