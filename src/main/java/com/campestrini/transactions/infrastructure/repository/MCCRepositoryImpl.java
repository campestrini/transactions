package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.MCCMongoRepository;
import com.campestrini.transactions.domain.model.Mcc;
import com.campestrini.transactions.domain.repository.MCCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MCCRepositoryImpl implements MCCRepository {

    private final MCCMongoRepository mccMongoRepository;
    @Override
    public Optional<Mcc> findByCode(String code) {
        return Optional.ofNullable(mccMongoRepository.findByCode(code));
    }
}
