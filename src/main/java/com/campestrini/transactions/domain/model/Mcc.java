package com.campestrini.transactions.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Document(collection = "mccs")
public class Mcc {
    private List<String> codes;
    private String balanceCode;
}
