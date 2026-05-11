package com.grazielleanaia.accounts.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//Single transaction must create multiple ledger entries
@Entity
@Table(name = "ledger_entries",
        indexes = {
                @Index(name = "idx_account_id", columnList = "account_id"),
                @Index(name = "idx_transaction_id", columnList = "transaction_id")
        })

public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ledger_type", nullable = false)
    private LedgerTypeEnum ledgerType;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public Ledger() {
    }

    public Ledger(UUID id, UUID accountId, UUID transactionId, UUID referenceId,
                  BigDecimal amount, LedgerTypeEnum ledgerType, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.referenceId = referenceId;
        this.amount = amount;
        this.ledgerType = ledgerType;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LedgerTypeEnum getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(LedgerTypeEnum ledgerType) {
        this.ledgerType = ledgerType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }
}
