package com.campestrini.transactions.service.strategy;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.service.AccountService;
import com.campestrini.transactions.service.AccountServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

@SpringBootTest
public class FallbackTransactionEvaluatorTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private FallbackTransactionEvaluator fallbackTransactionEvaluator;

    @Test
    public void testWithValidMerchantButAccountInconsistency_ShouldReturnRejected() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .totalAmount(new BigDecimal("1.0"))
                .build();

        doThrow(AccountServiceException.class)
                .when(accountService)
                .chargeAccount("CASH", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = fallbackTransactionEvaluator.evaluate(transactionDTO);

        verify(accountService).chargeAccount("CASH", transactionDTO.getTotalAmount());
        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }
    @Test
    public void testWithNoAccountInconsistency_ShouldReturnSuccess() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .totalAmount(new BigDecimal("1.0"))
                .build();

        doNothing().when(accountService).chargeAccount("CASH", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = fallbackTransactionEvaluator.evaluate(transactionDTO);

        verify(accountService).chargeAccount("CASH", transactionDTO.getTotalAmount());
        assertEquals(TransactionStatusCode.APPROVED.getCode(), transactionStatusDTO.getCode());
    }

}
