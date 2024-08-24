package com.campestrini.transactions.adapters.repository;

import com.campestrini.transactions.domain.model.Merchant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MerchantMongoRepository extends MongoRepository<Merchant, String> {

    Merchant findByName(String name);
}
