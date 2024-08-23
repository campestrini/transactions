package com.campestrini.transactions.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionStatusCode {

    APPROVED("00"),
    REJECTED("51"),
    ERROR("07");

    private final String code;

}
