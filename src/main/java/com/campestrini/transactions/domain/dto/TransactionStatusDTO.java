package com.campestrini.transactions.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransactionStatusDTO {
    private String code;
}
