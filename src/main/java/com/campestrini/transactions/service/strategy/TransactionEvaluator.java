package com.campestrini.transactions.service.strategy;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.Mcc;
import com.campestrini.transactions.domain.model.Merchant;
import com.campestrini.transactions.domain.repository.MerchantRepository;
import com.campestrini.transactions.infrastructure.repository.MCCRepositoryImpl;
import com.campestrini.transactions.service.AccountService;
import com.campestrini.transactions.service.AccountServiceException;
import com.campestrini.transactions.usecase.EvaluateTransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionEvaluator implements Evaluator {

    private final MCCRepositoryImpl mccRepository;

    private final AccountService accountService;

    private final MerchantRepository merchantRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionEvaluator.class);


    @Override
    public TransactionStatusDTO evaluate(TransactionDTO transactionDTO) {

        logger.info("[TransactionEvaluator] Fetching merchant with id '{}'", transactionDTO.getMerchant());
        Optional<Merchant> merchantResult = merchantRepository.findByName(transactionDTO.getMerchant());

        if (merchantResult.isPresent()) {
            logger.info("[TransactionEvaluator] Merchant with id '{}' exists", transactionDTO.getMerchant());
            Merchant merchant = merchantResult.get();
            return chargeAccount(merchant.getAccount(), transactionDTO.getTotalAmount());
        }

        logger.info("[TransactionEvaluator] Fetching MCC with id '{}'", transactionDTO.getMcc());
        Optional<Mcc> mccResult = mccRepository.findByCode(transactionDTO.getMcc());

        if (mccResult.isPresent()) {
            logger.info("[TransactionEvaluator] MCC with id '{}' exists", transactionDTO.getMcc());
            Mcc mcc = mccResult.get();
            return chargeAccount(mcc.getAccount(), transactionDTO.getTotalAmount());
        }

        return TransactionStatusDTO.builder().code(TransactionStatusCode.REJECTED.getCode()).build();
    }

    private TransactionStatusDTO chargeAccount(String account, BigDecimal totalAmount) {
        try {
            accountService.chargeAccount(account, totalAmount);
            return TransactionStatusDTO.builder().code(TransactionStatusCode.APPROVED.getCode()).build();
        } catch (AccountServiceException e) {
            return TransactionStatusDTO.builder().code(TransactionStatusCode.REJECTED.getCode()).build();
        }
    }
}