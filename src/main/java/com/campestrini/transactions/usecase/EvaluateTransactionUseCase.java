package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.CreateTransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.MCC;
import com.campestrini.transactions.domain.model.Seller;
import com.campestrini.transactions.infrastructure.repository.MCCRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluateTransactionUseCase {

    private final MCCRepositoryImpl mccRepository;

    public TransactionStatusDTO execute(CreateTransactionDTO createTransactionDTO) {

        Optional<Seller> result = Optional.of()

//        Optional<MCC> result = Optional.of(mccRepository.findByCode(createTransactionDTO.getMcc()));
//        if (result.isPresent()) {
//            MCC mcc = result.get();
//            BigDecimal total = mcc.getTotal();
//            BigDecimal totalAmount = createTransactionDTO.getTotalAmount();
//
//            if (total.compareTo(totalAmount) > 0) {
//                mcc.setTotal(total.subtract(totalAmount));
//            }
//            if(mcc.getTotal() > createTransactionDTO.getTotalAmount()) {
//
//            }

        }
    }
}
