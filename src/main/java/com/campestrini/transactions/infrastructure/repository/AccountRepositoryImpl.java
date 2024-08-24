package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.AccountMongoRepository;
import com.campestrini.transactions.domain.model.Account;
import com.campestrini.transactions.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountMongoRepository balanceRepository;
    @Override
    public Optional<Account> findByCode(String code) {
        return Optional.ofNullable(balanceRepository.findByCode(code));
    }

    public void save(Account account) {
        balanceRepository.save(account);
    }
}
