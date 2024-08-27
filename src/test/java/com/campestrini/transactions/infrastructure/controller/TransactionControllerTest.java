package com.campestrini.transactions.infrastructure.controller;


import com.campestrini.transactions.domain.dto.TransactionDTO;
import com.campestrini.transactions.domain.dto.TransactionStatusCode;
import com.campestrini.transactions.domain.dto.TransactionStatusDTO;
import com.campestrini.transactions.infrastructure.TransactionController;
import com.campestrini.transactions.usecase.EvaluateTransactionUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    EvaluateTransactionUseCase evaluateTransactionUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidation() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder().build();

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("Id is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("Account is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value("Total Amount must not be null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mcc").value("Mcc is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.merchant").value("Merchant is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fallback").value("Fallback is required"));
    }

    @ParameterizedTest
    @CsvSource({
            "-1.0, Total Amount must be positive number",
             "0.0, Total Amount must be positive number",
            "1.2333, Total Amount must have up to 10 integer digits and 2 decimal places",
            "12345678901.2, Total Amount must have up to 10 integer digits and 2 decimal places"

    })
    public void testTotalAmountValidation(String totalAmount, String validationMessage) throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id("id")
                .account("account")
                .totalAmount(new BigDecimal(totalAmount))
                .mcc("1234")
                .merchant("merchant")
                .fallback(false)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(validationMessage));
    }

    @Test
    public void testMccValidation() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id("id")
                .account("account")
                .totalAmount(new BigDecimal("1.0"))
                .mcc("1")
                .merchant("merchant")
                .fallback(false)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mcc").value("Mcc must have four characters"));
    }

    @Test
    public void testSuccess() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id("id")
                .account("account")
                .totalAmount(new BigDecimal("1.0"))
                .mcc("1234")
                .merchant("merchant")
                .fallback(false)
                .build();

        TransactionStatusDTO transactionStatusDTO = TransactionStatusDTO.builder()
                .code(TransactionStatusCode.APPROVED.getCode())
                .build();

        when(evaluateTransactionUseCase.execute(any(TransactionDTO.class))).thenReturn(transactionStatusDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(TransactionStatusCode.APPROVED.getCode()));
    }

    @Test
    public void testError() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id("id")
                .account("account")
                .totalAmount(new BigDecimal("1.0"))
                .mcc("1234")
                .merchant("merchant")
                .fallback(false)
                .build();

        when(evaluateTransactionUseCase.execute(any(TransactionDTO.class))).thenThrow(new RuntimeException("bang!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(TransactionStatusCode.ERROR.getCode()));
    }
}
