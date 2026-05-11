package com.grazielleanaia.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequest {

    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private UUID referenceId; // idempotency key

    public PaymentRequest() {
    }

    public PaymentRequest(UUID referenceId, BigDecimal amount, UUID toAccountId, UUID fromAccountId) {
        this.referenceId = referenceId;
        this.amount = amount;
        this.toAccountId = toAccountId;
        this.fromAccountId = fromAccountId;
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
}