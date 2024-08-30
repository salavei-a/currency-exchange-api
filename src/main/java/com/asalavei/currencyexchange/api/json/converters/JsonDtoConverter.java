package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Dto;
import com.asalavei.currencyexchange.api.json.JsonDto;

import java.util.Collection;

/**
 * Converter for converting between {@link JsonDto} and {@link Dto}
 */
public interface JsonDtoConverter<J extends JsonDto, D extends Dto> {
    D toDto(J jsonDto);

    J toJsonDto(D dto);

    Collection<J> toJsonDto(Collection<D> dtoCollection);
}
