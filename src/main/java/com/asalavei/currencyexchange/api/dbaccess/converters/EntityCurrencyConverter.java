package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dto.Currency;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Converter for converting between {@link EntityCurrency} and {@link Currency}
 */
public class EntityCurrencyConverter implements EntityDtoConverter<Integer, EntityCurrency, Currency>{
    /**
     * Converts the {@link Currency dto} to the {@link EntityCurrency}
     *
     * @param dto incoming the {@link Currency dto}
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
     * @param entity incoming the {@link EntityCurrency entity}
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


    @Override
    public Collection<Currency> toDto(Collection<EntityCurrency> entityCollection) {
        return entityCollection.stream().map(this::toDto).collect(Collectors.toList());
    }
}
