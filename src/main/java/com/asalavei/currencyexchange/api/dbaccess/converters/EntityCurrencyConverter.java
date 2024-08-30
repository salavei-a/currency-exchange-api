package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dto.Currency;

import java.util.Collection;

/**
 * Converter for converting between {@link EntityCurrency} and {@link Currency}
 */
public class EntityCurrencyConverter implements EntityDtoConverter<EntityCurrency, Currency> {

    /**
     * Converts the {@link Currency dto} to the {@link EntityCurrency}
     *
     * @param dto incoming the {@link Currency} to be converted
     * @return the converted {@link EntityCurrency}
     */
    @Override
    public EntityCurrency toEntity(Currency dto) {
        return EntityCurrency.builder()
                .id(dto.getId())
                .name(dto.getName())
                .code(dto.getCode())
                .sign(dto.getSign())
                .build();
    }

    /**
     * Converts the {@link EntityCurrency entity} to the {@link Currency}
     *
     * @param entity incoming the {@link EntityCurrency} to be converted
     * @return the converted {@link Currency}
     */
    @Override
    public Currency toDto(EntityCurrency entity) {
        return Currency.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .sign(entity.getSign())
                .build();
    }

    /**
     * Converts the collection of the {@link EntityCurrency entityCollection} to the collection of the {@link Currency}
     *
     * @param entityCollection the collection of the {@link EntityCurrency} to be converted
     * @return the converted collection of the {@link Currency}
     */
    @Override
    public Collection<Currency> toDto(Collection<EntityCurrency> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .toList();
    }
}
