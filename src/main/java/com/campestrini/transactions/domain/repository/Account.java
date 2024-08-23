package com.campestrini.transactions.domain.repository;

public interface Account {
    public com.campestrini.transactions.domain.model.Account findByCode(String code);
}
