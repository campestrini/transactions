package com.campestrini.transactions.adapters.repository;

import com.campestrini.transactions.domain.model.Mcc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MCCMongoRepository extends MongoRepository<Mcc, String> {
    Mcc findByCodesContaining(String code);
}
