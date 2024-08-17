package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateRepository extends Repository<EntityExchangeRate> {
    EntityExchangeRate findByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency);

    BigDecimal getRateByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency);

    EntityExchangeRate update(BigDecimal rate, Integer idBaseCurrency, Integer idTargetCurrency);
}
