package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.json.JsonExchange;

import java.util.Collection;
import java.util.stream.Collectors;

public class JsonExchangeConverter implements JsonDtoConverter<JsonExchange, Exchange> {

    private final JsonCurrencyConverter converter = new JsonCurrencyConverter();

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

    public JsonExchange toJsonDto(Exchange dto) {
        return JsonExchange.builder()
                .baseCurrency(converter.toJsonDto(dto.getBaseCurrency()))
                .targetCurrency(converter.toJsonDto(dto.getTargetCurrency()))
                .rate(dto.getRate())
                .amount(dto.getAmount())
                .convertedAmount(dto.getConvertedAmount())
                .build();
    }

    @Override
    public Collection<JsonExchange> toJsonDto(Collection<Exchange> dtoCollection) {
        return dtoCollection.stream().map(this::toJsonDto).collect(Collectors.toList());
    }
}
