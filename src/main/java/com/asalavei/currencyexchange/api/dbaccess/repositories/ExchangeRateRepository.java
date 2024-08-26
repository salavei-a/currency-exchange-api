package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.util.Optional;

public interface ExchangeRateRepository extends Repository<EntityExchangeRate> {
    Optional<EntityExchangeRate> findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);

    EntityExchangeRate update(EntityExchangeRate entity);
}
