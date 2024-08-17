package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeRateService extends BaseCrudService<ExchangeRate, EntityExchangeRate, EntityExchangeRateConverter, ExchangeRateRepository> {

    public ExchangeRateService(EntityExchangeRateConverter converter, ExchangeRateRepository repository) {
        super(converter, repository);
    }

    public ExchangeRate findByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency) {
        return converter.toDto(repository.findByCurrencyPair(idBaseCurrency, idTargetCurrency));
    }

    public BigDecimal getRateByCurrencyPair(Integer idBaseCurrency, Integer idTargetCurrency) {
        return repository.getRateByCurrencyPair(idBaseCurrency, idTargetCurrency);
    }

    public ExchangeRate update(BigDecimal rate, Integer idBaseCurrency, Integer idTargetCurrency) {
        return converter.toDto(repository.update(rate, idBaseCurrency, idTargetCurrency));
    }
}
