package com.campestrini.transactions.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionDTO {
    @NotBlank(message = "Id is required")
    private String id;

    @NotBlank(message = "Account is required")
    private String account;

    @NotNull(message = "Total Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total Amount must be positive number")
    @Digits(integer = 10, fraction = 2, message = "Total Amount must have up to 10 integer digits and 2 decimal places")
    private BigDecimal totalAmount;

    @NotBlank(message = "Mcc is required")
    @Length(min = 4, max = 4, message = "Mcc must have four characters")
    private String mcc;

    @NotBlank(message = "Merchant is required")
    private String merchant;

    @NotNull(message = "Fallback is required")
    private Boolean fallback;

}
