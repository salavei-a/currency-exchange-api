package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dto.Dto;

import java.util.Collection;

public interface EntityDtoConverter<E extends Entity, D extends Dto> {
    E toEntity(D dto);

    D toDto(E entity);

    Collection<D> toDto(Collection<E> entityCollection);
}
