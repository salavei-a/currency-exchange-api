package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.CurrencyRepository;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dto.Currency;

public class CurrencyService extends BaseCrudService<Currency, EntityCurrency, EntityCurrencyConverter, CurrencyRepository> {

    public CurrencyService(EntityCurrencyConverter converter, CurrencyRepository repository) {
        super(converter, repository);
    }

    public Currency findByCode(String code) {
        return converter.toDto(repository.findByCode(code));
    }
}
