package com.grazielleanaia.accounts.controller;


import com.grazielleanaia.accounts.dto.AccountTransferRequest;
import com.grazielleanaia.accounts.dto.CreateAccountRequest;
import com.grazielleanaia.accounts.dto.DepositRequest;
import com.grazielleanaia.accounts.entity.Account;
import com.grazielleanaia.accounts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")

public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody AccountTransferRequest transferRequest) {
        accountService.transfer(transferRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest accountRequest) {
        Account account1 = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(account1, HttpStatus.CREATED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositRequest depositRequest) {
        accountService.createDeposit(depositRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
