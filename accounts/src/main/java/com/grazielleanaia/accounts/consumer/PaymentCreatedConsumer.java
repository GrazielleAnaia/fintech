package com.grazielleanaia.accounts.consumer;

import com.grazielleanaia.accounts.dto.AccountTransferRequest;
import com.grazielleanaia.accounts.dto.PaymentCreatedEvent;
import com.grazielleanaia.accounts.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class PaymentCreatedConsumer {

    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(PaymentCreatedConsumer.class);

    public PaymentCreatedConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @RetryableTopic(attempts = "4",
            include = {SQLException.class, CannotAcquireLockException.class},
            dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
            backOff = @BackOff (delay = 2000, multiplier = 2))
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
