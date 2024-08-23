package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Mcc;

public interface MCCRepository {
    public Mcc findByCode(String code);
}
