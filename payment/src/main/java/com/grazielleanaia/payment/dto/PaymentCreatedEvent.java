package com.grazielleanaia.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCreatedEvent (UUID transactionId,
                                   UUID referenceId,
                                   UUID fromAccountId,
                                   UUID toAccountId,
                                   BigDecimal amount) {

}
