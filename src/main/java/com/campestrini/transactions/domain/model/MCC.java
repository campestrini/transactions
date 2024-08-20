package com.campestrini.transactions.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Document(collection = "mccs")
public class MCC {
    private List<String> codes;
    private String description;
    private BigDecimal total;
}
