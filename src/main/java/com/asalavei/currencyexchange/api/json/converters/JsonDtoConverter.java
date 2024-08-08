package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.BaseDto;
import com.asalavei.currencyexchange.api.json.BaseJsonDto;

import java.util.Collection;

public interface JsonDtoConverter<I extends Comparable<I>, J extends BaseJsonDto<I>, D extends BaseDto<I>>{
    D toDto(J jsonDto);

    J toJsonDto(D dto);

    Collection<J> toJsonDto(Collection<D> dtoCollection);
}
