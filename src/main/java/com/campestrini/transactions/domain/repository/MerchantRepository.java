package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Merchant;

import java.util.Optional;

public interface MerchantRepository {
    Optional<Merchant> findByName(String name);
}
