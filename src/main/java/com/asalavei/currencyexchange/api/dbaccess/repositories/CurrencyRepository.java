package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

import java.util.Optional;

public interface CurrencyRepository extends Repository<EntityCurrency> {
    Optional<EntityCurrency> findByCode(String code);
}
