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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionEvaluatorTest {

    @Mock
    private MCCRepositoryImpl mccRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private TransactionEvaluator transactionEvaluator;

    @Test
    public void testWithMerchantAndAccountInconsistency_ShouldReturnRejected() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .merchant("UBER EATS                   SAO PAULO BR")
                .totalAmount(new BigDecimal("1.0"))
                .build();

        Merchant merchant = Merchant.builder()
                .name("UBER EATS                   SAO PAULO BR")
                .account("FOOD")
                .build();

        when(merchantRepository.findByName("UBER EATS                   SAO PAULO BR")).thenReturn(Optional.of(merchant));

        doThrow(AccountServiceException.class)
                .when(accountService)
                .chargeAccount("FOOD", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        verify(merchantRepository).findByName("UBER EATS                   SAO PAULO BR");
        verify(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());
        verify(mccRepository, never()).findByCode(any(String.class));

        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }

    @Test
    public void testWithMerchantAndNoAccountInconsistency_ShouldReturnApproved() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .merchant("UBER EATS                   SAO PAULO BR")
                .totalAmount(new BigDecimal("1.0"))
                .build();

        Merchant merchant = Merchant.builder()
                .name("UBER EATS                   SAO PAULO BR")
                .account("FOOD")
                .build();

        when(merchantRepository.findByName("UBER EATS                   SAO PAULO BR")).thenReturn(Optional.of(merchant));

        doNothing().when(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        verify(merchantRepository).findByName("UBER EATS                   SAO PAULO BR");
        verify(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());
        verify(mccRepository, never()).findByCode(any(String.class));

        assertEquals(TransactionStatusCode.APPROVED.getCode(), transactionStatusDTO.getCode());
    }

    @Test
    public void testWithMccAndAccountInconsistency_ShouldReturnRejected() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .merchant("invalid merchant")
                .mcc("5411")
                .totalAmount(new BigDecimal("1.0"))
                .build();

        when(merchantRepository.findByName("invalid merchant")).thenReturn(Optional.empty());

        Mcc mcc = Mcc.builder()
                .codes(List.of("5411"))
                .account("FOOD")
                .build();

        when(mccRepository.findByCode("5411")).thenReturn(Optional.of(mcc));

        doThrow(AccountServiceException.class)
                .when(accountService)
                .chargeAccount("FOOD", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        verify(merchantRepository).findByName("invalid merchant");
        verify(mccRepository).findByCode("5411");
        verify(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());

        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }

    @Test
    public void testWithMccAndNoAccountInconsistency_ShouldReturnApproved() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .merchant("invalid merchant")
                .mcc("5411")
                .totalAmount(new BigDecimal("1.0"))
                .build();

        when(merchantRepository.findByName("invalid merchant")).thenReturn(Optional.empty());

        Mcc mcc = Mcc.builder()
                .codes(List.of("5411"))
                .account("FOOD")
                .build();

        when(mccRepository.findByCode("5411")).thenReturn(Optional.of(mcc));

        doNothing().when(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        verify(merchantRepository).findByName("invalid merchant");
        verify(mccRepository).findByCode("5411");
        verify(accountService).chargeAccount("FOOD", transactionDTO.getTotalAmount());

        assertEquals(TransactionStatusCode.APPROVED.getCode(), transactionStatusDTO.getCode());
    }

    @Test
    public void testWithNoAccountAndNoMcc() throws AccountServiceException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .merchant("invalid merchant")
                .mcc("invalid mcc")
                .totalAmount(new BigDecimal("1.0"))
                .build();

        when(merchantRepository.findByName("invalid merchant")).thenReturn(Optional.empty());
        when(mccRepository.findByCode("invalid mcc")).thenReturn(Optional.empty());

        TransactionStatusDTO transactionStatusDTO = transactionEvaluator.evaluate(transactionDTO);

        verify(merchantRepository).findByName("invalid merchant");
        verify(mccRepository).findByCode("invalid mcc");
        verify(accountService, never()).chargeAccount(any(String.class), any(BigDecimal.class));

        assertEquals(TransactionStatusCode.REJECTED.getCode(), transactionStatusDTO.getCode());
    }

}
