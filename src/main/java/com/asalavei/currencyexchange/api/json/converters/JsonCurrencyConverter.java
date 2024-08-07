package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;

/**
 * Converter for converting between {@link JsonCurrency} and {@link Currency}
 */
public class JsonCurrencyConverter {
    /**
     * Converts the {@link JsonCurrency jsonDto} to the {@link Currency}
     *
     * @param jsonDto incoming the {@link JsonCurrency jsonDto}
     * @return the converted {@link Currency}
     */
    public Currency toDto(JsonCurrency jsonDto) {
        return Currency.builder()
                .id(jsonDto.getId())
                .code(jsonDto.getCode())
                .fullName(jsonDto.getFullName())
                .sign(jsonDto.getSign())
                .build();
    }

    /**
     * Converts the {@link Currency dto} to the {@link JsonCurrency}
     *
     * @param dto incoming the {@link Currency dto}
     * @return the converted {@link JsonCurrency}
     */
    public JsonCurrency toJsonDto(Currency dto) {
        return JsonCurrency.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .fullName(dto.getFullName())
                .sign(dto.getSign())
                .build();
    }
}
