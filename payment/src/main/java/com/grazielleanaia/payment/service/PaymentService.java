package com.grazielleanaia.payment.service;


import com.grazielleanaia.payment.client.HttpAccountClient;
import com.grazielleanaia.payment.dto.AccountTransferRequest;
import com.grazielleanaia.payment.dto.PaymentCreatedEvent;
import com.grazielleanaia.payment.dto.PaymentRequest;
import com.grazielleanaia.payment.entity.TransactionStatusEnum;
import com.grazielleanaia.payment.entity.TransactionTypeEnum;
import com.grazielleanaia.payment.entity.Transactions;
import com.grazielleanaia.payment.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        Transactions existingTx =
                transactionRepository.findByReferenceId(request.getReferenceId())
                        .orElseGet(() -> {
                            try {
                                return createPendingTransaction(request);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

        //If already processed, return complete
        if (existingTx.getStatus() == TransactionStatusEnum.COMPLETED) {
            log.info("Payment has been completed, reference-id = {}", request.getReferenceId());
            return existingTx;
        }


        try {
            log.info("Calling AccountService for transfer, transferId={}", existingTx.getReferenceId());

            //Messaging with Kafka
            PaymentCreatedEvent createdEvent = new PaymentCreatedEvent(existingTx.getId(),
                    request.getReferenceId(), request.getFromAccountId(), request.getToAccountId(),
                    request.getAmount());
            kafkaTemplate.send("payment-created-topic", createdEvent);


            //HttpInterface with WebClient
            httpAccountClient.transfer(new AccountTransferRequest(
                    request.getFromAccountId(),
                    request.getToAccountId(),
                    request.getAmount(),
                    existingTx.getId(),
                    request.getReferenceId()
            ));
            existingTx.setStatus(TransactionStatusEnum.COMPLETED);
            transactionRepository.save(existingTx);

            log.info("Payment completed successfully, txId={}", existingTx.getId());

        } catch (Exception ex) {

            log.error("Payment failed, txId={}", existingTx.getId(), ex);
            existingTx.setStatus(TransactionStatusEnum.FAILED);
            transactionRepository.save(existingTx);
            throw new RuntimeException("Payment failed", ex);
        }
        return existingTx;
    }

    private Transactions createPendingTransaction(PaymentRequest request) throws Exception {
        try {
            Transactions transaction = new Transactions();
            transaction.setReferenceId(request.getReferenceId());
            transaction.setStatus(TransactionStatusEnum.PENDING);
            transaction.setType(TransactionTypeEnum.PAYMENT);
            return transactionRepository.save(transaction);
        } catch (Exception ex) {
            return transactionRepository
                    .findByReferenceId(request.getReferenceId())
                    .orElseThrow(() -> ex);
        }
    }
}
