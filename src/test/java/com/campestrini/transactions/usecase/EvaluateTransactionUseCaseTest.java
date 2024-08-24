package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.model.Mcc;
import com.campestrini.transactions.infrastructure.repository.AccountRepositoryImpl;
import com.campestrini.transactions.infrastructure.repository.MCCRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EvaluateTransactionUseCaseTest {

    @Mock
    private MCCRepositoryImpl mccRepository;

    @Mock
    private AccountRepositoryImpl accountRepository;

    @InjectMocks
    private EvaluateTransactionUseCase evaluateTransactionUseCase;

    @Test
    public void givenInvalidMcc_thenShouldReturnRejectedTransaction() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .mcc("INVALID_MCC")
                .build();

        when(mccRepository.findByCode("INVALID_MCC")).thenReturn(Optional.empty());

        TransactionStatusDTO transactionStatusDTO = evaluateTransactionUseCase.execute(transactionDTO);

        verify(mccRepository).findByCode("INVALID_MCC");
        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }
    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0",
            "1.0, 2.0"
    })
    public void givenValidMccAndAccountWithSufficientBalance_thenShouldReturnApprovedTransaction(String totalAmount, String balance) {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .mcc("5411")
                .totalAmount(new BigDecimal(totalAmount))
                .build();

        Mcc mcc = Mcc.builder()
                .codes(List.of("5411"))
                .account("FOOD")
                .build();

        when(mccRepository.findByCode("5411")).thenReturn(Optional.of(mcc));

        Account account = Account.builder()
                .code("FOOD")
                .balance(new BigDecimal(balance))
                .build();

        when(accountRepository.findByCode("FOOD")).thenReturn(Optional.of(account));

        TransactionStatusDTO transactionStatusDTO = evaluateTransactionUseCase.execute(transactionDTO);

        verify(mccRepository).findByCode("5411");
        verify(accountRepository).findByCode("FOOD");
        assertEquals(TransactionStatusCode.APPROVED.getCode(), transactionStatusDTO.getCode());
    }

    @Test
    public void givenValidMccAndAccountWithInsufficientBalance_thenShouldReturnRejectTransaction() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .mcc("5411")
                .totalAmount(new BigDecimal("2.0"))
                .build();

        Mcc mcc = Mcc.builder()
                .codes(List.of("5411"))
                .account("FOOD")
                .build();

        when(mccRepository.findByCode("5411")).thenReturn(Optional.of(mcc));

        Account account = Account.builder()
                .code("FOOD")
                .balance(new BigDecimal("1.0"))
                .build();

        when(accountRepository.findByCode("FOOD")).thenReturn(Optional.of(account));

        TransactionStatusDTO transactionStatusDTO = evaluateTransactionUseCase.execute(transactionDTO);

        verify(mccRepository).findByCode("5411");
        verify(accountRepository).findByCode("FOOD");
        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }


}
