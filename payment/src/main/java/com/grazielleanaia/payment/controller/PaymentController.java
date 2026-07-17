package com.grazielleanaia.payment.controller;


import com.grazielleanaia.payment.dto.PaymentRequest;
import com.grazielleanaia.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, String>>> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.processPayment(paymentRequest)
                .handle((metadata, ex) -> {

                    Map<String, String> body = new HashMap<>();

                    if (ex != null) {
                        // Extract the root cause if it's wrapped in a CompletionException
                        String errorMsg = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage();
                        body.put("status", "FAILED");
                        body.put("error", Objects.requireNonNull(errorMsg, "Unknown error"));

                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
                    }

                    body.put("status", "ACCEPTED");
                    body.put("transactionId", String.valueOf(paymentRequest.getReferenceId()));
                    body.put("partition", String.valueOf(metadata.partition()));

                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
                });
    }
}
