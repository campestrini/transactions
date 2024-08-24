package com.campestrini.transactions.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@Builder
@Document(collection = "sellers")
public class Seller {
    @Id
    private String id;
    private String name;
    private String description;
}
