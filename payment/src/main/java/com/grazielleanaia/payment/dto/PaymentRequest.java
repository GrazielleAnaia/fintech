package com.grazielleanaia.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequest {

    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private UUID referenceId; // idempotency key
//    private UUID transactionId;

    public PaymentRequest() {
    }

    public PaymentRequest(UUID fromAccountId, UUID toAccountId, BigDecimal amount, UUID referenceId, UUID transactionId) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.referenceId = referenceId;
//        this.transactionId = transactionId;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(UUID fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(UUID toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

//    public UUID getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(UUID transactionId) {
//        this.transactionId = transactionId;
//    }
}