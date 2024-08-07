package com.asalavei.currencyexchange.api.dao.converters;

import com.asalavei.currencyexchange.api.dao.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.dto.Currency;

/**
 * Converter for converting between {@link EntityCurrency} and {@link Currency}
 */
public class EntityCurrencyConverter {
    /**
     * Converts the {@link Currency dto} to the {@link EntityCurrency}
     *
     * @param dto incoming the {@link Currency dto}
     * @return the converted {@link EntityCurrency}
     */
    public EntityCurrency toEntity(Currency dto) {
        return EntityCurrency.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .fullName(dto.getFullName())
                .sign(dto.getSign())
                .build();
    }

    /**
     * Converts the {@link EntityCurrency entity} to the {@link Currency}
     *
     * @param entity incoming the {@link EntityCurrency entity}
     * @return the converted {@link Currency}
     */
    public Currency toDto(EntityCurrency entity) {
        return Currency.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .fullName(entity.getFullName())
                .sign(entity.getSign())
                .build();
    }
}
