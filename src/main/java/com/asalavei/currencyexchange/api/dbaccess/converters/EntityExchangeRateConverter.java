package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Converter for converting between {@link EntityExchangeRate} and {@link ExchangeRate}
 */
public class EntityExchangeRateConverter implements EntityDtoConverter<Integer, EntityExchangeRate, ExchangeRate> {
    /**
     * Converts the {@link ExchangeRate dto} to the {@link EntityExchangeRate}
     *
     * @param dto incoming the {@link ExchangeRate dto}
     * @return the converted {@link EntityExchangeRate}
     */
    @Override
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
    @Override
    public ExchangeRate toDto(EntityExchangeRate entity) {
        return ExchangeRate.builder()
                .id(entity.getId())
                .baseCurrencyId(entity.getBaseCurrencyId())
                .targetCurrencyId(entity.getTargetCurrencyId())
                .rate(entity.getRate())
                .build();
    }

    @Override
    public Collection<ExchangeRate> toDto(Collection<EntityExchangeRate> entityCollection) {
        return entityCollection.stream().map(this::toDto).collect(Collectors.toList());
    }
}
