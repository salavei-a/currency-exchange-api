package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dto.Dto;

import java.util.Collection;

/**
 * Converter for converting between {@link Entity} and {@link Dto}
 *
 * @param <E> the type of {@link Entity}
 * @param <D> the type of {@link Dto}
 */
public interface EntityDtoConverter<E extends Entity, D extends Dto> {
    E toEntity(D dto);

    D toDto(E entity);

    Collection<D> toDto(Collection<E> entityCollection);
}
