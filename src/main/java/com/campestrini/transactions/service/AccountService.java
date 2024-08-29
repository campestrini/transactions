package com.campestrini.transactions.service;

import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.repository.AccountRepository;
import com.campestrini.transactions.service.strategy.TransactionEvaluator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public void chargeAccount(String accountCode, BigDecimal totalAmount) throws AccountServiceException {
        Optional<Account> result = accountRepository.findByCode(accountCode);

        logger.info("[AccountService] Fetching '{}' account", accountCode);

        if(result.isEmpty()) {
            logger.info("[AccountService] Account with code '{}' not found", accountCode);

            throw new AccountServiceException("Account not found");
        }

        Account account = result.get();

        if(!this.hasSufficientBalance(account, totalAmount)) {
            logger.info("[AccountService] Account with code '{}' has insufficient balance", accountCode);
            throw new AccountServiceException("Insufficient balance");
        }

        updateAccountBalance(account, totalAmount);
    }

    private boolean hasSufficientBalance(Account account, BigDecimal totalAmount) {
        return account.getBalance().compareTo(totalAmount) >= 0;
    }

    private void updateAccountBalance(Account account, BigDecimal totalAmount ) {
        BigDecimal updatedBalance = account.getBalance().subtract(totalAmount);
        logger.info("[AccountService] Updating balance of '{}' account to '{}' ", account.getCode(), updatedBalance);
        account.setBalance(updatedBalance);
        accountRepository.save(account);
    }
}
