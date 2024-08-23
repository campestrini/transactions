package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Seller;

public interface SellerRepository {

    public Seller findByName(String name);
}
