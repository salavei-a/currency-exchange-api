package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;

import java.util.Collection;

/**
 * Converter for converting between {@link JsonCurrency} and {@link Currency}
 */
public class JsonCurrencyConverter implements JsonDtoConverter<JsonCurrency, Currency> {

    /**
     * Converts the {@link JsonCurrency jsonDto} to the {@link Currency}
     *
     * @param jsonDto incoming the {@link JsonCurrency} to be converted
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
     * @param dto incoming the {@link Currency} to be converted
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

    /**
     * Converts the collection of the {@link Currency dtoCollection} to the collection of the {@link JsonCurrency}
     *
     * @param dtoCollection the collection of the {@link Currency} to be converted
     * @return the converted collection of the {@link JsonCurrency}
     */
    @Override
    public Collection<JsonCurrency> toJsonDto(Collection<Currency> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toJsonDto)
                .toList();

    }
}
