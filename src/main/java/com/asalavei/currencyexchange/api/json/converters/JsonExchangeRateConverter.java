package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;

import java.util.Collection;

/**
 * Converter for converting between {@link JsonExchangeRate} and {@link ExchangeRate}
 */
public class JsonExchangeRateConverter implements JsonDtoConverter<JsonExchangeRate, ExchangeRate> {

    private final JsonCurrencyConverter converter = new JsonCurrencyConverter();

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
                .baseCurrency(converter.toDto(jsonDto.getBaseCurrency()))
                .targetCurrency(converter.toDto(jsonDto.getTargetCurrency()))
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
                .baseCurrency(converter.toJsonDto(dto.getBaseCurrency()))
                .targetCurrency(converter.toJsonDto(dto.getTargetCurrency()))
                .rate(dto.getRate())
                .build();
    }

    /**
     * Converts the collection of the {@link ExchangeRate dtoCollection} to the collection of the {@link JsonExchangeRate}
     *
     * @param dtoCollection the collection of the {@link ExchangeRate} to be converted
     * @return the converted collection of the {@link JsonExchangeRate}
     */
    @Override
    public Collection<JsonExchangeRate> toJsonDto(Collection<ExchangeRate> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toJsonDto)
                .toList();
    }
}
