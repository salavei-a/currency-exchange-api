package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.ExchangeRateDao;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

public class ExchangeRateService extends BaseCrudService<Integer, ExchangeRate, EntityExchangeRate, EntityExchangeRateConverter, ExchangeRateDao> {

    public ExchangeRateService(ExchangeRateDao entityDao, EntityExchangeRateConverter converter) {
        super(entityDao, converter);
    }
}
