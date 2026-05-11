package com.grazielleanaia.payment.service;


import com.grazielleanaia.payment.client.HttpAccountClient;
import com.grazielleanaia.payment.dto.AccountTransferRequest;
import com.grazielleanaia.payment.dto.PaymentRequest;
import com.grazielleanaia.payment.entity.TransactionStatusEnum;
import com.grazielleanaia.payment.entity.TransactionTypeEnum;
import com.grazielleanaia.payment.entity.Transactions;
import com.grazielleanaia.payment.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final TransactionRepository transactionRepository;
    private final HttpAccountClient httpAccountClient;


    public PaymentService(TransactionRepository transactionRepository, HttpAccountClient httpAccountClient) {
        this.transactionRepository = transactionRepository;
        this.httpAccountClient = httpAccountClient;
    }


    public Transactions processPayment(PaymentRequest request) {

        //1. Idempotency DB-enforced
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

        //if already processed, return complete
        if (existingTx.getStatus() == TransactionStatusEnum.COMPLETED) {
            log.info("Payment has been completed, reference-id = {}", request.getReferenceId());

            return existingTx;
        }

        //HttpInterface with WebClient
        try {
            log.info("Calling AccountService for transfer, transferId={}", existingTx.getReferenceId());
            httpAccountClient.transfer(new AccountTransferRequest(
                    request.getFromAccountId(),
                    request.getToAccountId(),
                    request.getAmount()));
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
            Transactions tx = new Transactions();
            tx.setReferenceId(request.getReferenceId());
            tx.setStatus(TransactionStatusEnum.PENDING);
            tx.setType(TransactionTypeEnum.PAYMENT);
            return transactionRepository.save(tx);
        } catch (Exception ex) {
            return transactionRepository
                    .findByReferenceId(request.getReferenceId())
                    .orElseThrow(() -> ex);
        }
    }
}
