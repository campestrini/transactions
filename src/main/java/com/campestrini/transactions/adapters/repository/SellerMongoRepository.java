package com.campestrini.transactions.adapters.repository;

import com.campestrini.transactions.domain.model.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerMongoRepository extends MongoRepository<Seller, String> {

    Seller findByName(String name);
}
