package com.campestrini.transactions.adapters.repository;

import com.campestrini.transactions.domain.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountMongoRepository extends MongoRepository<Account, String> {

    Account findByCode(String code);
}