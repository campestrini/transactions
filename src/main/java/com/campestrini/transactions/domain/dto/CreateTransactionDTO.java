package com.campestrini.transactions.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTransactionDTO {
    @NotBlank(message = "Account is required")
    private String id;

    @NotBlank(message = "Account is required")
    private String account;

    @NotEmpty
    @DecimalMin(value = "0.0", inclusive = false, message = "Total Amount must be positive")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Total Amount must have up to 10 integer digits and 2 decimal places")
    private BigDecimal totalAmount;

    @NotBlank(message = "Mcc is required")
    @Length(min = 4, max = 4, message = "MCC must have four characters")
    private String mcc;

    @NotBlank(message = "Mcc is required")
    private String merchant;

}
