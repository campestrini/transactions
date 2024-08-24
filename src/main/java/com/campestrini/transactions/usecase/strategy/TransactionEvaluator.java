package com.campestrini.transactions.usecase.strategy;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.Mcc;
import com.campestrini.transactions.domain.model.Merchant;
import com.campestrini.transactions.domain.repository.MerchantRepository;
import com.campestrini.transactions.infrastructure.repository.MCCRepositoryImpl;
import com.campestrini.transactions.usecase.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionEvaluator implements Evaluator {

    private final MCCRepositoryImpl mccRepository;

    private final AccountService accountService;

    private final MerchantRepository merchantRepository;

    @Override
    public TransactionStatusDTO evaluate(TransactionDTO transactionDTO) {
        Optional<Merchant> merchantResult = merchantRepository.findByName(transactionDTO.getMerchant());
        if (merchantResult.isPresent()) {
            Merchant merchant = merchantResult.get();
            return chargeAccount(merchant.getAccount(), transactionDTO.getTotalAmount());
        }

        Optional<Mcc> mccResult = mccRepository.findByCode(transactionDTO.getMcc());
        if (mccResult.isPresent()) {
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