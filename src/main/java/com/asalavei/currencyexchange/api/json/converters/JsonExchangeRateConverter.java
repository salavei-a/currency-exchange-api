package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;

/**
 * Converter for converting between {@link JsonExchangeRate} and {@link ExchangeRate}
 */
public class JsonExchangeRateConverter {
    /**
     * Converts the {@link JsonExchangeRate jsonDto} to the {@link ExchangeRate}
     *
     * @param jsonDto incoming the {@link JsonExchangeRate jsonDto}
     * @return the converted {@link ExchangeRate}
     */
    public ExchangeRate toDto(JsonExchangeRate jsonDto) {
        return ExchangeRate.builder()
                .id(jsonDto.getId())
                .baseCurrencyId(jsonDto.getBaseCurrencyId())
                .targetCurrencyId(jsonDto.getTargetCurrencyId())
                .rate(jsonDto.getRate())
                .build();
    }

    /**
     * Converts the {@link ExchangeRate dto} to the {@link JsonExchangeRate}
     *
     * @param dto incoming the {@link ExchangeRate dto}
     * @return the converted {@link JsonExchangeRate}
     */
    public JsonExchangeRate toJsonDto(ExchangeRate dto) {
        return JsonExchangeRate.builder()
                .id(dto.getId())
                .baseCurrencyId(dto.getBaseCurrencyId())
                .targetCurrencyId(dto.getTargetCurrencyId())
                .rate(dto.getRate())
                .build();
    }
}
