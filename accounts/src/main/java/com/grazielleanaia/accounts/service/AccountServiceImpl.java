package com.grazielleanaia.accounts.service;


import com.grazielleanaia.accounts.dto.AccountTransferRequest;
import com.grazielleanaia.accounts.dto.CreateAccountRequest;
import com.grazielleanaia.accounts.dto.DepositRequest;
import com.grazielleanaia.accounts.entity.Account;
import com.grazielleanaia.accounts.entity.Ledger;
import com.grazielleanaia.accounts.entity.LedgerTypeEnum;
import com.grazielleanaia.accounts.repository.AccountRepository;
import com.grazielleanaia.accounts.repository.LedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, LedgerRepository ledgerRepository) {
        this.accountRepository = accountRepository;
        this.ledgerRepository = ledgerRepository;
    }

    @Caching(evict = {@CacheEvict(value = "balances", key = "#transferRequest.fromAccountId"),
            @CacheEvict(value = "balances", key = "#transferRequest.toAccountId")})
    @Override
    public void transfer(AccountTransferRequest transferRequest) {

        //Idempotency check
        if (ledgerRepository.existsByReferenceId(transferRequest.getReferenceId())) {
            logger.info("Duplicate request detected, skipping. refId={}", transferRequest.getReferenceId());
            return;
        }

        Account from = accountRepository.findById(transferRequest.getFromAccountId()).orElseThrow(() ->
                new RuntimeException("From account not found"));
        Account to = accountRepository.findById(transferRequest.getToAccountId()).orElseThrow(() ->
                new RuntimeException("To account not found"));

        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BigDecimal balance = calculateBalance(from.getId());

        if (balance.compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        logger.info("Transfer from {} to {}", from.getId(), to.getId());

        Ledger debit = new Ledger();
        debit.setAccountId(from.getId());
        debit.setTransactionId(transferRequest.getTransactionId());
        debit.setReferenceId(transferRequest.getReferenceId());
        debit.setLedgerType(LedgerTypeEnum.DEBIT);
        debit.setAmount(transferRequest.getAmount());

        Ledger credit = new Ledger();
        credit.setAccountId(to.getId());
        credit.setTransactionId(transferRequest.getTransactionId());
        credit.setReferenceId(transferRequest.getReferenceId());
        credit.setLedgerType(LedgerTypeEnum.CREDIT);
        credit.setAmount(transferRequest.getAmount());

        ledgerRepository.save(credit);
        ledgerRepository.save(debit);
    }

    @Override
    public Account createAccount(CreateAccountRequest request) {
        accountRepository.findByUserIdAndCurrency(
                        request.getUserId(), request.getCurrency())
                .ifPresent(a -> {
                    throw new RuntimeException("Account already exists");
                });
        Account account = new Account();
        account.setCurrency(request.getCurrency());
        account.setUserId(request.getUserId());
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public void createDeposit(DepositRequest depositRequest) {
        Account account = accountRepository.findByIdForUpdate(depositRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (depositRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        logger.info("Depositing {} into account {}", depositRequest.getAmount(), account.getId());

        Ledger credit = new Ledger();
        credit.setAccountId(account.getId());
        credit.setLedgerType(LedgerTypeEnum.CREDIT);
        credit.setAmount(depositRequest.getAmount());

        ledgerRepository.save(credit);
    }

    //Balance=∑Credits−∑Debits
    @Cacheable(value = "balances", key = "#accountId")
    public BigDecimal calculateBalance(UUID accountId) {
        logger.info("Calculating balance from DB for account {}", accountId);
        BigDecimal credits = ledgerRepository.sumByAccountAndType(accountId, LedgerTypeEnum.CREDIT);
        BigDecimal debits = ledgerRepository.sumByAccountAndType(accountId, LedgerTypeEnum.DEBIT);
        return credits.subtract(debits);
    }
}
