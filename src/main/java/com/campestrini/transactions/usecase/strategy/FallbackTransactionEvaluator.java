package com.campestrini.transactions.usecase.strategy;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.usecase.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FallbackTransactionEvaluator implements Evaluator{

    private final String CASH_ACCOUNT_CODE = "CASH";
    private final AccountService accountService;

    @Override
    public TransactionStatusDTO evaluate(TransactionDTO transactionDTO) {
        try {
            accountService.chargeAccount(CASH_ACCOUNT_CODE, transactionDTO.getTotalAmount());
            return TransactionStatusDTO.builder().code(TransactionStatusCode.APPROVED.getCode()).build();
        } catch (AccountServiceException e) {
            return TransactionStatusDTO.builder().code(TransactionStatusCode.REJECTED.getCode()).build();
        }
    }
}
