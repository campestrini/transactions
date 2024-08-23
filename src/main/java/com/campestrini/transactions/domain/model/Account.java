package com.campestrini.transactions.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "mccs")
public class Account {
    private String code;
    private BigDecimal value;
}
