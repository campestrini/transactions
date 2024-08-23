package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.SellerMongoRepository;
import com.campestrini.transactions.domain.model.Seller;
import com.campestrini.transactions.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerMCCRepositoryImpl implements SellerRepository {

    private final SellerMongoRepository sellerMongoRepository;

    @Override
    public Seller findByName(String name) {
        sellerMongoRepository.findByName(name);
    }
}
