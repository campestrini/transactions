package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.repository.AccountRepository;
import com.campestrini.transactions.usecase.strategy.AccountServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void chargeAccount(String accountCode, BigDecimal totalAmount) throws AccountServiceException {
        Optional<Account> result = accountRepository.findByCode(accountCode);

        if(result.isEmpty()) {
            throw new AccountServiceException("Account Not found");
        }

        Account account = result.get();

        if(!this.hasSufficientBalance(account, totalAmount)) {
            throw new AccountServiceException("Insufficient Balance");
        }

        updateAccountBalance(account, totalAmount);
    }

    private boolean hasSufficientBalance(Account account, BigDecimal totalAmount) {
        return account.getBalance().compareTo(totalAmount) >= 0;
    }

    private void updateAccountBalance(Account account, BigDecimal totalAmount ) {
        BigDecimal updatedBalance = account.getBalance().subtract(totalAmount);
        account.setBalance(updatedBalance);
        accountRepository.save(account);
    }
}
