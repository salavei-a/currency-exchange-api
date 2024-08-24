package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

public class ExchangeRateService extends BaseCrudService<ExchangeRate, EntityExchangeRate, EntityExchangeRateConverter, ExchangeRateRepository> {

    public ExchangeRateService(EntityExchangeRateConverter converter, ExchangeRateRepository repository) {
        super(converter, repository);
    }

    @Override
    public ExchangeRate create(ExchangeRate entity) {
        return save(EntityExchangeRate.builder()
                .baseCurrency(EntityCurrency.builder()
                        .code(entity.getBaseCurrency().getCode())
                        .build())
                .targetCurrency(EntityCurrency.builder()
                        .code(entity.getTargetCurrency().getCode())
                        .build())
                .rate(entity.getRate())
                .build());
    }

    public ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        return converter.toDto(repository.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode));
    }

    public ExchangeRate update(ExchangeRate entity) {
        return converter.toDto(repository.update(converter.toEntity(entity)));
    }
}
