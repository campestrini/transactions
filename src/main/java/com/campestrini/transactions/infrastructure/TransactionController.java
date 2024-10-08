package com.campestrini.transactions.infrastructure;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.usecase.EvaluateTransactionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final EvaluateTransactionUseCase evaluateTransactionUseCase;

    @PostMapping
    public ResponseEntity<TransactionStatusDTO> evaluate(@Valid @RequestBody TransactionDTO transactionDTO) {
            TransactionStatusDTO transactionStatusDTO = evaluateTransactionUseCase.execute(transactionDTO);
            return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDTO);
    }
}
