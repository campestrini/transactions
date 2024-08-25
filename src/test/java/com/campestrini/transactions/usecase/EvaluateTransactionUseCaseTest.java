package com.campestrini.transactions.usecase;

import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.service.strategy.FallbackTransactionEvaluator;
import com.campestrini.transactions.service.strategy.TransactionEvaluator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EvaluateTransactionUseCaseTest {

    @Mock
    private TransactionEvaluator transactionEvaluator;

    @Mock
    private FallbackTransactionEvaluator fallbackTransactionEvaluator;

    @InjectMocks
    private EvaluateTransactionUseCase evaluateTransactionUseCase;

    private static Stream<Arguments> transactionsTestCases() {
        return Stream.of(
                Arguments.of(TransactionStatusCode.APPROVED.getCode()),
                Arguments.of(TransactionStatusCode.REJECTED.getCode())
        );
    }

    @ParameterizedTest
    @MethodSource("transactionsTestCases")
    public void testExecuteWithNormalTransaction(String transactionCode) {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .fallback(false)
                .build();

        TransactionStatusDTO transactionStatusDTO = TransactionStatusDTO.builder()
                .code(transactionCode)
                .build();

        when(transactionEvaluator.evaluate(any(TransactionDTO.class))).thenReturn(transactionStatusDTO);

        TransactionStatusDTO evaluation = evaluateTransactionUseCase.execute(transactionDTO);

        assertEquals(transactionCode, evaluation.getCode());
        verify(transactionEvaluator).evaluate(transactionDTO);
        verify(fallbackTransactionEvaluator, never()).evaluate(any(TransactionDTO.class));
    }

    @ParameterizedTest
    @MethodSource("transactionsTestCases")
    public void testExecuteWithFallbackTransaction(String transactionCode) {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .fallback(true)
                .build();

        TransactionStatusDTO transactionStatusDTO = TransactionStatusDTO.builder()
                .code(TransactionStatusCode.REJECTED.getCode())
                .build();

        when(transactionEvaluator.evaluate(any(TransactionDTO.class))).thenReturn(transactionStatusDTO);

        TransactionStatusDTO fallbackTransactionStatusDTO = TransactionStatusDTO.builder()
                .code(transactionCode)
                .build();

        when(fallbackTransactionEvaluator.evaluate(any(TransactionDTO.class))).thenReturn(fallbackTransactionStatusDTO);

        TransactionStatusDTO evaluation = evaluateTransactionUseCase.execute(transactionDTO);

        assertEquals(transactionCode, evaluation.getCode());

        verify(transactionEvaluator).evaluate(transactionDTO);
        verify(fallbackTransactionEvaluator).evaluate(transactionDTO);
    }
}
