package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.CurrencyDao;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dto.Currency;

public class CurrencyService extends BaseCrudService<Integer, Currency, EntityCurrency, EntityCurrencyConverter, CurrencyDao> {

    public CurrencyService(CurrencyDao entityDao, EntityCurrencyConverter converter) {
        super(entityDao, converter);
    }
}
