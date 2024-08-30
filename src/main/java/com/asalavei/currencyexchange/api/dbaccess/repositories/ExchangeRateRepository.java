package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.util.Optional;

/**
 * Repository for accessing exchange rate entities
 */
public interface ExchangeRateRepository extends Repository<EntityExchangeRate> {
    Optional<EntityExchangeRate> findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);

    EntityExchangeRate update(EntityExchangeRate entity);
}
