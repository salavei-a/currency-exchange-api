package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Converter for converting between {@link JsonCurrency} and {@link Currency}
 */
public class JsonCurrencyConverter implements JsonDtoConverter<Integer, JsonCurrency, Currency> {
    /**
     * Converts the {@link JsonCurrency jsonDto} to the {@link Currency}
     *
     * @param jsonDto incoming the {@link JsonCurrency jsonDto}
     * @return the converted {@link Currency}
     */
    @Override
    public Currency toDto(JsonCurrency jsonDto) {
        return Currency.builder()
                .id(jsonDto.getId())
                .name(jsonDto.getName())
                .code(jsonDto.getCode())
                .sign(jsonDto.getSign())
                .build();
    }

    /**
     * Converts the {@link Currency dto} to the {@link JsonCurrency}
     *
     * @param dto incoming the {@link Currency dto}
     * @return the converted {@link JsonCurrency}
     */
    @Override
    public JsonCurrency toJsonDto(Currency dto) {
        return JsonCurrency.builder()
                .id(dto.getId())
                .name(dto.getName())
                .code(dto.getCode())
                .sign(dto.getSign())
                .build();
    }

    @Override
    public Collection<JsonCurrency> toJsonDto(Collection<Currency> dtoCollection) {
        return dtoCollection.stream().map(this::toJsonDto).collect(Collectors.toList());

    }
}
