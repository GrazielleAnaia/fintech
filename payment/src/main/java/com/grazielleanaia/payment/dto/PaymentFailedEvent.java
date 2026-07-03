package com.grazielleanaia.payment.dto;

import java.util.UUID;

public record PaymentFailedEvent(UUID transactionId,
                                 UUID referenceId,
                                 String reason) {
}
