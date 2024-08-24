package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.SellerMongoRepository;
import com.campestrini.transactions.domain.model.Seller;
import com.campestrini.transactions.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SellerMCCRepositoryImpl implements SellerRepository {

    private final SellerMongoRepository sellerMongoRepository;

    @Override
    public Optional<Seller> findByName(String name) {
        return Optional.ofNullable(sellerMongoRepository.findByName(name));
    }
}
