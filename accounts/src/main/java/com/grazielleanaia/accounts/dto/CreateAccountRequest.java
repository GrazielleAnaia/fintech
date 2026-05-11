package com.grazielleanaia.accounts.dto;

public class CreateAccountRequest {

    private Long userId;
    private String currency;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(Long userId, String currency) {
        this.userId = userId;
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
