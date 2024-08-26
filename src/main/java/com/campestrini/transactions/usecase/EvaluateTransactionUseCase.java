package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.service.strategy.FallbackTransactionEvaluator;
import com.campestrini.transactions.service.strategy.TransactionEvaluator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateTransactionUseCase {

    private final TransactionEvaluator transactionEvaluator;
    private final FallbackTransactionEvaluator fallbackTransactionEvaluator;

    private static final Logger logger = LoggerFactory.getLogger(EvaluateTransactionUseCase.class);


    public TransactionStatusDTO execute(TransactionDTO transactionDTO) {

        logger.info("Evaluating transaction: {}", transactionDTO);

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        if (!transactionDTO.getFallback()) {
            return transactionStatusDTO;
        }

        if (transactionStatusDTO.getCode().equals(TransactionStatusCode.REJECTED.getCode())) {
            return fallbackTransactionEvaluator.evaluate(transactionDTO);
        }

        return transactionStatusDTO;
    }
}
