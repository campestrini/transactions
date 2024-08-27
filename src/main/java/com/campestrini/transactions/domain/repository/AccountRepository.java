package com.campestrini.transactions.domain.repository;

import com.campestrini.transactions.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByCode(String code);

    void save(Account account);
}
