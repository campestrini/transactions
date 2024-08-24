package com.campestrini.transactions.infrastructure.repository;

import com.campestrini.transactions.adapters.repository.MerchantMongoRepository;
import com.campestrini.transactions.domain.model.Merchant;
import com.campestrini.transactions.domain.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MerchantMCCRepositoryImpl implements MerchantRepository {

    private final MerchantMongoRepository merchantMongoRepository;

    @Override
    public Optional<Merchant> findByName(String name) {
        return Optional.ofNullable(merchantMongoRepository.findByName(name));
    }
}
