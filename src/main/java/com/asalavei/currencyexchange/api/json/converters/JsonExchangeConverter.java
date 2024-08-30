package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.json.JsonExchange;

import java.util.Collection;

/**
 * Converter for converting between {@link JsonExchange} and {@link Exchange}
 */
public class JsonExchangeConverter implements JsonDtoConverter<JsonExchange, Exchange> {

    private final JsonCurrencyConverter converter = new JsonCurrencyConverter();

    /**
     * Converts the {@link JsonExchange jsonDto} to the {@link Exchange}
     *
     * @param jsonDto incoming the {@link JsonExchange} to be converted
     * @return the converted {@link Exchange}
     */
    @Override
    public Exchange toDto(JsonExchange jsonDto) {
        return Exchange.builder()
                .baseCurrency(converter.toDto(jsonDto.getBaseCurrency()))
                .targetCurrency(converter.toDto(jsonDto.getTargetCurrency()))
                .rate(jsonDto.getRate())
                .amount(jsonDto.getAmount())
                .convertedAmount(jsonDto.getConvertedAmount())
                .build();
    }

    /**
     * Converts the {@link Exchange dto} to the {@link JsonExchange}
     *
     * @param dto incoming the {@link Exchange} to be converted
     * @return the converted {@link JsonExchange}
     */
    public JsonExchange toJsonDto(Exchange dto) {
        return JsonExchange.builder()
                .baseCurrency(converter.toJsonDto(dto.getBaseCurrency()))
                .targetCurrency(converter.toJsonDto(dto.getTargetCurrency()))
                .rate(dto.getRate())
                .amount(dto.getAmount())
                .convertedAmount(dto.getConvertedAmount())
                .build();
    }

    /**
     * Converts the collection of the {@link Exchange dtoCollection} to the collection of the {@link JsonExchange}
     *
     * @param dtoCollection the collection of the {@link Exchange} to be converted
     * @return the converted collection of the {@link JsonExchange}
     */
    @Override
    public Collection<JsonExchange> toJsonDto(Collection<Exchange> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toJsonDto)
                .toList();
    }
}
