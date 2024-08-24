package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.model.Mcc;
import com.campestrini.transactions.infrastructure.repository.AccountRepositoryImpl;
import com.campestrini.transactions.infrastructure.repository.MCCRepositoryImpl;
import com.campestrini.transactions.infrastructure.repository.SellerMCCRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluateTransactionUseCase {

    private final MCCRepositoryImpl mccRepository;
    private final SellerMCCRepositoryImpl sellerRepository;
    private final AccountRepositoryImpl accountRepository;

    public TransactionStatusDTO execute(TransactionDTO transactionDTO) {
        return mccRepository.findByCode(transactionDTO.getMcc())
                .flatMap(mcc -> accountRepository.findByCode(mcc.getAccount()))
                .filter(account -> hasSufficientBalance(account, transactionDTO))
                .map(account -> approveTransaction(account, transactionDTO))
                .orElseGet(() -> TransactionStatusDTO.builder()
                        .code(TransactionStatusCode.REJECTED.getCode())
                        .build());
    }

    private boolean hasSufficientBalance(Account account, TransactionDTO transactionDTO) {
        return account.getBalance().compareTo(transactionDTO.getTotalAmount()) >= 0;
    }

    private TransactionStatusDTO approveTransaction(Account account, TransactionDTO transactionDTO) {
        updateAccountBalance(account, transactionDTO);
        return TransactionStatusDTO.builder()
                .code(TransactionStatusCode.APPROVED.getCode())
                .build();
    }

    private void updateAccountBalance(Account account, TransactionDTO transactionDTO) {
        BigDecimal updatedBalance = account.getBalance().subtract(transactionDTO.getTotalAmount());
        account.setBalance(updatedBalance);
        accountRepository.save(account);
    }
}
