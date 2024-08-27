package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Mcc;

import java.util.Optional;

public interface MCCRepository {
    Optional<Mcc> findByCode(String code);
}
