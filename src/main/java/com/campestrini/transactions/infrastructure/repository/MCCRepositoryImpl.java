package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.MCCMongoRepository;
import com.campestrini.transactions.domain.model.MCC;
import com.campestrini.transactions.domain.repository.MCCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MCCRepositoryImpl implements MCCRepository {

    private final MCCMongoRepository mccMongoRepository;
    @Override
    public MCC findByCode(String code) {
        return mccMongoRepository.findByCodesContaining(code);
    }
}
