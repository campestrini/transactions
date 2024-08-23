package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.AccountMongoRepository;
import com.campestrini.transactions.domain.repository.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements Account {

    private final AccountMongoRepository balanceRepository;
    @Override
    public com.campestrini.transactions.domain.model.Account findByCode(String code) {
        return balanceRepository.findByCode(code);
    }

    public void save(com.campestrini.transactions.domain.model.Account account) {
        balanceRepository.save(account);
    }
}
