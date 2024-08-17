package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Dto;
import com.asalavei.currencyexchange.api.json.JsonDto;

import java.util.Collection;

public interface JsonDtoConverter<J extends JsonDto, D extends Dto>{
    D toDto(J jsonDto);

    J toJsonDto(D dto);

    Collection<J> toJsonDto(Collection<D> dtoCollection);
}
