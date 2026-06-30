package com.grazielleanaia.accounts.consumer;

import com.grazielleanaia.accounts.dto.PaymentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class PaymentDLQConsumer {

    private final Logger log = LoggerFactory.getLogger(PaymentCreatedConsumer.class);


    @KafkaListener(topics = "payment-created-topic-dlt")
    public void consume(PaymentCreatedEvent event) {
        log.error("Permanent failure {}", event.referenceId());

        //Persist failure
        //Publish a PaymentFailedEvent from DLQ listener so the Payment service can mark the transaction as FAILED
    }
}
