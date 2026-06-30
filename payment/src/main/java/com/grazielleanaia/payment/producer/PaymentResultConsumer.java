package com.grazielleanaia.payment.producer;


import com.grazielleanaia.payment.dto.PaymentCompletedEvent;
import com.grazielleanaia.payment.entity.TransactionStatusEnum;
import com.grazielleanaia.payment.entity.Transactions;
import com.grazielleanaia.payment.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentResultConsumer {
    private final TransactionRepository transactionRepository;

    public PaymentResultConsumer(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @KafkaListener(topics = "payment-completed-topic", groupId = "payment-group")
    public void consumeCompleted(PaymentCompletedEvent paymentCompletedEvent) {
        Transactions transaction = transactionRepository.findById(paymentCompletedEvent.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (transaction.getStatus() == TransactionStatusEnum.COMPLETED) {
            return;
        }
        transaction.setStatus(TransactionStatusEnum.COMPLETED);
        transactionRepository.save(transaction);
    }
}
