package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Seller;

import java.util.Optional;

public interface SellerRepository {

    public Optional<Seller> findByName(String name);
}
