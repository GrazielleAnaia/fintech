package com.grazielleanaia.payment.client;

import com.grazielleanaia.payment.dto.AccountTransferRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


//Http Interface with WebClient

@HttpExchange("/api/v1/accounts")
public interface HttpAccountClient {

    @PostExchange("/transfer")
    void transfer(@RequestBody AccountTransferRequest request);
}
