package com.grazielleanaia.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositRequest {

    private UUID accountId;

    private BigDecimal amount;

    private UUID referenceId;


    public DepositRequest() {
    }

    public DepositRequest(UUID accountId, BigDecimal amount, UUID referenceId) {
        this.accountId = accountId;
        this.amount = amount;
        this.referenceId = referenceId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
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
