package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.MCC;

public interface MCCRepository {
    public MCC findByCode(String code);
}
