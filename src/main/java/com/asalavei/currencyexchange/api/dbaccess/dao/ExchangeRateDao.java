package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateDao extends BaseEntityDao<Integer, EntityExchangeRate> {
    EntityExchangeRate findByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency);

    BigDecimal getRateByCurrencyPair(int idBaseCurrency, int idTargetCurrency);

    EntityExchangeRate update(BigDecimal rate, int idBaseCurrency, int idTargetCurrency);
}
