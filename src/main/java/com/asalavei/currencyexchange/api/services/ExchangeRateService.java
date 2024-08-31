package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessage;

/**
 * Service for managing exchange rate data
 */
public class ExchangeRateService extends BaseCrudService<ExchangeRate, EntityExchangeRate, EntityExchangeRateConverter, ExchangeRateRepository> {

    public ExchangeRateService(EntityExchangeRateConverter converter, ExchangeRateRepository repository) {
        super(converter, repository);
    }

    public ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        return converter.toDto(repository.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new CENotFoundException(String.format(ExceptionMessage.EXCHANGE_RATE_NOT_FOUND, baseCurrencyCode, targetCurrencyCode))));
    }

    public ExchangeRate update(ExchangeRate entity) {
        return converter.toDto(repository.update(converter.toEntity(entity)));
    }
}
