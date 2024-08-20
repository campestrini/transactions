package com.campestrini.transactions.adapters.repository;

import com.campestrini.transactions.domain.model.MCC;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MCCMongoRepository extends MongoRepository<MCC, String> {

    MCC findByCodesContaining(String code);
}
