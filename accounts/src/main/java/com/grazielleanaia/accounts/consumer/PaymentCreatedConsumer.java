package com.grazielleanaia.accounts.consumer;

import com.grazielleanaia.accounts.dto.AccountTransferRequest;
import com.grazielleanaia.accounts.dto.PaymentCreatedEvent;
import com.grazielleanaia.accounts.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCreatedConsumer {

    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(PaymentCreatedConsumer.class);

    public PaymentCreatedConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(topics = "payment-created-topic",
            groupId = "accounts-group")
    public void consume(PaymentCreatedEvent event) {
        logger.info("Received Payment Event = " +
                event.referenceId());

        accountService.transfer(new AccountTransferRequest(
                event.fromAccountId(),
                event.toAccountId(),
                event.amount(),
                event.referenceId(),
                event.transactionId()));
    }
}
