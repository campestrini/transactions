package com.campestrini.transactions.service;

import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void accountNotFound_ShouldReturnException() {
        String accountCode = "ACCOUNT_CODE";
        BigDecimal totalAmount = new BigDecimal("1.0");

        when(accountRepository.findByCode(accountCode)).thenReturn(Optional.empty());

        AccountServiceException exception = assertThrows(AccountServiceException.class, () ->
                accountService.chargeAccount(accountCode, totalAmount));

        verify(accountRepository).findByCode(accountCode);
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void accountHasInsufficientBalance_ShouldReturnException() {
        String accountCode = "ACCOUNT_CODE";
        BigDecimal totalAmount = new BigDecimal("2.0");

        Account account = Account.builder()
                .code(accountCode)
                .balance(new BigDecimal("1.0"))
                .build();

        when(accountRepository.findByCode(accountCode)).thenReturn(Optional.of(account));

        AccountServiceException exception = assertThrows(AccountServiceException.class, () ->
                accountService.chargeAccount(accountCode, totalAmount));

        verify(accountRepository).findByCode(accountCode);
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void accountHasSufficientBalance() throws AccountServiceException {
        String accountCode = "ACCOUNT_CODE";
        BigDecimal totalAmount = new BigDecimal("1.0");

        Account account = Account.builder()
                .code(accountCode)
                .balance(new BigDecimal("2.0"))
                .build();

        when(accountRepository.findByCode(accountCode)).thenReturn(Optional.of(account));
        accountService.chargeAccount(accountCode, totalAmount);

        verify(accountRepository).findByCode(accountCode);
        assertEquals(new BigDecimal("1.0"), account.getBalance());
    }

}
