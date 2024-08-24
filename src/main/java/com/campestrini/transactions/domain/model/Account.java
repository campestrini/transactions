package com.campestrini.transactions.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Document(collection = "accounts")
@AllArgsConstructor
public class Account {
    @Id
    private String id;
    private String code;
    private BigDecimal balance;
}
