package com.campestrini.transactions.service.strategy;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;

import java.math.BigDecimal;

public interface Evaluator {

    TransactionStatusDTO evaluate(TransactionDTO transactionDTO);
}
