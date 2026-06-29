package com.grazielleanaia.payment.dto;

import java.util.UUID;

public record PaymentCompletedEvent(UUID transactionId,
                                    UUID referenceId) {
}
