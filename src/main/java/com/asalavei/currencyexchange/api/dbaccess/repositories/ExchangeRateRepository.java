package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateRepository extends Repository<EntityExchangeRate> {
    EntityExchangeRate findByCurrencyPairCodes(String baseCurrencyCode, String targetCurrencyCode);

    BigDecimal getRateByCurrencyPairIds(Integer baseCurrencyId, Integer targetCurrencyId);

    EntityExchangeRate updateRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
}
