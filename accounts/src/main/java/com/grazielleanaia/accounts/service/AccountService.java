package com.grazielleanaia.accounts.service;

import com.grazielleanaia.accounts.dto.AccountTransferRequest;
import com.grazielleanaia.accounts.dto.CreateAccountRequest;
import com.grazielleanaia.accounts.dto.DepositRequest;
import com.grazielleanaia.accounts.entity.Account;
import com.grazielleanaia.accounts.entity.Ledger;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {

    void transfer(AccountTransferRequest transferRequest);

    Account createAccount(CreateAccountRequest request);

    void createDeposit(DepositRequest depositRequest);

    BigDecimal calculateBalance(UUID accountId);
}
