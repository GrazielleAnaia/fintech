package com.grazielleanaia.payment.service;


import com.grazielleanaia.payment.client.HttpAccountClient;
import com.grazielleanaia.payment.dto.PaymentCreatedEvent;
import com.grazielleanaia.payment.dto.PaymentRequest;
import com.grazielleanaia.payment.entity.TransactionStatusEnum;
import com.grazielleanaia.payment.entity.TransactionTypeEnum;
import com.grazielleanaia.payment.entity.Transactions;
import com.grazielleanaia.payment.repository.TransactionRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final TransactionRepository transactionRepository;
    private final HttpAccountClient httpAccountClient;
    private final KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate;


    public PaymentService(TransactionRepository transactionRepository, HttpAccountClient httpAccountClient,
                          KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.httpAccountClient = httpAccountClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Transactions processPayment(PaymentRequest request) {

        //Idempotency DB-enforced
        log.info("Idempotency DB-enforced");
        Optional<Transactions> existingTransaction = transactionRepository.findByReferenceId(request.getReferenceId());

        if (existingTransaction.isPresent()) {
            log.info("Payment already exists, referenceId={}", request.getReferenceId());
            return existingTransaction.get();
        }

        Transactions transaction = createPendingTransaction(request);

        //Messaging with Kafka
        PaymentCreatedEvent createdEvent = new PaymentCreatedEvent(transaction.getId(),
                request.getReferenceId(), request.getFromAccountId(), request.getToAccountId(),
                request.getAmount());
        ProducerRecord<String, PaymentCreatedEvent> record = new ProducerRecord<>(
                "payment-created-topic", createdEvent);
        record.headers().add("referenceId", createdEvent.referenceId().toString().getBytes());
        record.headers().add("transactionId", createdEvent.transactionId().toString().getBytes());
        kafkaTemplate.send(record);

        return transaction;
    }

    //Create DB record, persist transaction as PENDING status
    private Transactions createPendingTransaction(PaymentRequest request) {

        Transactions transaction = new Transactions();
        transaction.setReferenceId(request.getReferenceId());
        transaction.setStatus(TransactionStatusEnum.PENDING);
        transaction.setType(TransactionTypeEnum.PAYMENT);

        return transactionRepository.save(transaction);

    }
}
