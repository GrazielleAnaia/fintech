package com.grazielleanaia.payment.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reference_id", columnNames = "reference_id")})
//One client request must create only one transaction
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; //transactionId (internal)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionTypeEnum type;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId; //idempotency key (external-safe, globally unique)

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Transactions() {
    }

    public Transactions(UUID id, TransactionStatusEnum status,
                        TransactionTypeEnum type, UUID referenceId, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionStatusEnum status) {
        this.status = status;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }
}
