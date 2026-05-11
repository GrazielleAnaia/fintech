package com.grazielleanaia.payment.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AccountServiceClientConfig {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceClientConfig.class);

    @Bean
    public HttpAccountClient httpAccountClient(WebClient.Builder webClientBuilder) {

        WebClient webClient = webClientBuilder
                .baseUrl("http://localhost:8081")
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        HttpAccountClient httpAccountClient = factory.createClient(HttpAccountClient.class);
        return httpAccountClient;
    }
}
