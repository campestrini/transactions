package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
    public Optional<Account> findByCode(String code);

    public void save(Account account);
}
