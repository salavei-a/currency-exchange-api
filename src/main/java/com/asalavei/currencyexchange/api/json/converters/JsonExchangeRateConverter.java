package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;

import java.util.Collection;
import java.util.List;

/**
 * Converter for converting between {@link JsonExchangeRate} and {@link ExchangeRate}
 */
public class JsonExchangeRateConverter implements JsonDtoConverter<Integer, JsonExchangeRate, ExchangeRate> {
    /**
     * Converts the {@link JsonExchangeRate jsonDto} to the {@link ExchangeRate}
     *
     * @param jsonDto incoming the {@link JsonExchangeRate jsonDto}
     * @return the converted {@link ExchangeRate}
     */
    @Override
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
    @Override
    public JsonExchangeRate toJsonDto(ExchangeRate dto) {
        return JsonExchangeRate.builder()
                .id(dto.getId())
                .baseCurrencyId(dto.getBaseCurrencyId())
                .targetCurrencyId(dto.getTargetCurrencyId())
                .rate(dto.getRate())
                .build();
    }

    @Override
    public Collection<JsonExchangeRate> toJsonDto(Collection<ExchangeRate> dtoCollection) {
        return List.of();
    }
}
