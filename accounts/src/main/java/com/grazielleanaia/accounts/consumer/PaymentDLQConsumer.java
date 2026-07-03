package com.grazielleanaia.accounts.consumer;

import com.grazielleanaia.accounts.dto.PaymentCreatedEvent;
import com.grazielleanaia.accounts.dto.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//Spring creates another Kafka listener to DLT

@Component
public class PaymentDLQConsumer {

    private final Logger log = LoggerFactory.getLogger(PaymentCreatedConsumer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentDLQConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "payment-created-topic-dlt")
    public void consume(PaymentCreatedEvent event) {
        log.error("Permanent failure {}", event.referenceId());

        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                event.transactionId(), event.referenceId(), "Permanent failure");

        //Publish a PaymentFailedEvent from DLQ listener so the Payment service can mark the transaction as FAILED
        kafkaTemplate.send("payment-failed-topic", paymentFailedEvent);
    }
}
