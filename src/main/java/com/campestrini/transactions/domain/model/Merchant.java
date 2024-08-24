package com.campestrini.transactions.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@Document(collection = "merchants")
public class Merchant {
    @Id
    private String id;
    private String name;
    private String account;
}
