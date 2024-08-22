package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeRateService extends BaseCrudService<ExchangeRate, EntityExchangeRate, EntityExchangeRateConverter, ExchangeRateRepository> {

    public ExchangeRateService(EntityExchangeRateConverter converter, ExchangeRateRepository repository) {
        super(converter, repository);
    }

    public ExchangeRate create(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        return save(EntityExchangeRate.builder()
                .baseCurrency(EntityCurrency.builder().code(baseCurrencyCode).build())
                .targetCurrency(EntityCurrency.builder().code(targetCurrencyCode).build())
                .rate(rate)
                .build());
    }

    public ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        return converter.toDto(repository.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode));
    }

    public ExchangeRate updateRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        return converter.toDto(repository.updateRate(baseCurrencyCode, targetCurrencyCode, rate));
    }
}
