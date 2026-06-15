package com.grazielleanaia.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCreatedEvent(UUID transactionId,
                                  UUID referenceId,
                                  UUID fromAccountId,
                                  UUID toAccountId,
                                  BigDecimal amount) {


    public PaymentCreatedEvent {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public PaymentCreatedEvent(
            UUID fromAccountId,
            UUID toAccountId,
            BigDecimal amount) {

        this(
                UUID.randomUUID(),
                UUID.randomUUID(),
                fromAccountId,
                toAccountId,
                amount
        );
    }

}
