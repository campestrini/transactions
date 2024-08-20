package com.campestrini.transactions.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@Document(collection = "mccs")
public class Seller {
    private String name;
    private String description;
}
