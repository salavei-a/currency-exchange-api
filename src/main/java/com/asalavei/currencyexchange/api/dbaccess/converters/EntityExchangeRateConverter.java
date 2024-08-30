package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;

import java.util.Collection;

/**
 * Converter for converting between {@link EntityExchangeRate} and {@link ExchangeRate}
 */
public class EntityExchangeRateConverter implements EntityDtoConverter<EntityExchangeRate, ExchangeRate> {

    private final EntityCurrencyConverter converter = new EntityCurrencyConverter();

    /**
     * Converts the {@link ExchangeRate dto} to the {@link EntityExchangeRate}
     *
     * @param dto incoming the {@link ExchangeRate} to be converted
     * @return the converted {@link EntityExchangeRate}
     */
    @Override
    public EntityExchangeRate toEntity(ExchangeRate dto) {
        return EntityExchangeRate.builder()
                .id(dto.getId())
                .baseCurrency(converter.toEntity(dto.getBaseCurrency()))
                .targetCurrency(converter.toEntity(dto.getTargetCurrency()))
                .rate(dto.getRate())
                .build();
    }

    /**
     * Converts the {@link EntityExchangeRate entity} to the {@link ExchangeRate}
     *
     * @param entity incoming the {@link EntityExchangeRate} to be converted
     * @return the converted {@link ExchangeRate}
     */
    @Override
    public ExchangeRate toDto(EntityExchangeRate entity) {
        return ExchangeRate.builder()
                .id(entity.getId())
                .baseCurrency(converter.toDto(entity.getBaseCurrency()))
                .targetCurrency(converter.toDto(entity.getTargetCurrency()))
                .rate(entity.getRate())
                .build();
    }

    /**
     * Converts the collection of the {@link EntityExchangeRate entityCollection} to the collection of the {@link ExchangeRate}
     *
     * @param entityCollection the collection of the {@link EntityExchangeRate} to be converted
     * @return the converted collection of the {@link ExchangeRate}
     */
    @Override
    public Collection<ExchangeRate> toDto(Collection<EntityExchangeRate> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .toList();
    }
}
