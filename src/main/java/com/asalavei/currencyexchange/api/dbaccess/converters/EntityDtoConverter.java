package com.asalavei.currencyexchange.api.dbaccess.converters;

import com.asalavei.currencyexchange.api.dbaccess.entities.BaseEntity;
import com.asalavei.currencyexchange.api.dto.BaseDto;

import java.util.Collection;

public interface EntityDtoConverter<I extends Comparable<I>, E extends BaseEntity<I>, D extends BaseDto<I>> {
    E toEntity(D dto);

    D toDto(E entity);

    Collection<D> toDto(Collection<E> entityCollection);


}
