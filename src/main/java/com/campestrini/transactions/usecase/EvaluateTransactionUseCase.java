package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.CreateTransactionDTO;
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

    public TransactionStatusDTO execute(CreateTransactionDTO createTransactionDTO) {

        Optional<Mcc> mcc = findMcc();

//        if (mcc.isPresent()) {
//            String code = mcc.get().getBalanceCode();
//
//            Optional<Account> balanceResult = findAccount(code);
//
//                if (balanceResult.isPresent()) {
//                    Account account = balanceResult.get();
//                    BigDecimal accountValue = account.getValue();
//
//                    if (accountHasSufficientBalance(accountValue, createTransactionDTO.getTotalAmount())) {
//                        BigDecimal newBalanceValue = accountValue.subtract(createTransactionDTO.getTotalAmount());
//                        account.setValue(newBalanceValue);
//                        accountRepository.save(account);
//
//                        return TransactionStatusDTO.builder().code(TransactionStatusCode.APPROVED.getCode()).build();
//                    } else {
//                        if (createTransactionDTO.getFallback()) {
//                            Optional<Account> cashAccount = findAccount("CASH");
//                            BigDecimal cashValue = cashAccount.getValue();
//
//                            if (accountHasSufficientBalance(cashValue, createTransactionDTO.getTotalAmount())) {
//                                BigDecimal newBalanceValue = cashValue.subtract(createTransactionDTO.getTotalAmount());
//                                cashAccount.setValue(newBalanceValue);
//                                accountRepository.save(cashAccount);
//
//                                return TransactionStatusDTO.builder().code(TransactionStatusCode.APPROVED.getCode()).build();
//                            }
//                        }
//                    }
//            }
//        } else {
//            // Busca MCC por cliente
//        }
//
//        return null;
//    }

    private Optional<Account> findAccount(String code) {
        return Optional.ofNullable(accountRepository.findByCode(code));
    }

    private Optional<Mcc> findMcc(CreateTransactionDTO createTransactionDTO) {
        return Optional.ofNullable(mccRepository.findByCode(createTransactionDTO.getMcc()));
    }

    private boolean accountHasSufficientBalance(BigDecimal accountValue, BigDecimal totalAmount) {
        return accountValue.compareTo(totalAmount) > 0;
    }
}
