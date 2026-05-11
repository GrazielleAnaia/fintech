package com.grazielleanaia.accounts.repository;


import com.grazielleanaia.accounts.entity.Ledger;
import com.grazielleanaia.accounts.entity.LedgerTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;


@Repository
public interface LedgerRepository extends JpaRepository<Ledger, UUID> {

    @Query("""
    SELECT COALESCE(SUM(l.amount), 0)
    FROM Ledger l
    WHERE l.accountId = :accountId AND l.ledgerType = :type
""")
    BigDecimal sumByAccountAndType(UUID accountId, LedgerTypeEnum type);

    public boolean existsByReferenceId(UUID referenceId);
}
