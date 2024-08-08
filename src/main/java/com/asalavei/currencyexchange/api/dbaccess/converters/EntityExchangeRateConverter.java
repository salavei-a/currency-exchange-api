package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

/**
 * Converter for converting between {@link EntityExchangeRate} and {@link ExchangeRate}
 */
public class EntityExchangeRateConverter {
    /**
     * Converts the {@link ExchangeRate dto} to the {@link EntityExchangeRate}
     *
     * @param dto incoming the {@link ExchangeRate dto}
     * @return the converted {@link EntityExchangeRate}
     */
    public EntityExchangeRate toEntity(ExchangeRate dto) {
        return EntityExchangeRate.builder()
                .id(dto.getId())
                .baseCurrencyId(dto.getBaseCurrencyId())
                .targetCurrencyId(dto.getTargetCurrencyId())
                .rate(dto.getRate())
                .build();
    }

    /**
     * Converts the {@link EntityExchangeRate entity} to the {@link ExchangeRate}
     *
     * @param entity incoming the {@link EntityExchangeRate entity}
     * @return the converted {@link ExchangeRate}
     */
    public ExchangeRate toDto(EntityExchangeRate entity) {
        return ExchangeRate.builder()
                .id(entity.getId())
                .baseCurrencyId(entity.getBaseCurrencyId())
                .targetCurrencyId(entity.getTargetCurrencyId())
                .rate(entity.getRate())
                .build();
    }
}
