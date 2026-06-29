package com.grazielleanaia.accounts.dto;

import java.util.UUID;

public record PaymentCompletedEvent(UUID transactionId,
                                    UUID referenceId) {
}
