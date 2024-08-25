package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.service.strategy.FallbackTransactionEvaluator;
import com.campestrini.transactions.service.strategy.TransactionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateTransactionUseCase {

    private final TransactionEvaluator transactionEvaluator;
    private final FallbackTransactionEvaluator fallbackTransactionEvaluator;


    public TransactionStatusDTO execute(TransactionDTO transactionDTO) {
        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        if (!transactionDTO.getFallback()) {
            return transactionStatusDTO;
        }

        if (transactionStatusDTO.getCode().equals(TransactionStatusCode.REJECTED)) {
            return fallbackTransactionEvaluator.evaluate(transactionDTO);
        }

        return transactionStatusDTO;
    }
}
