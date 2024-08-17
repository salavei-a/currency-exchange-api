package com.asalavei.currencyexchange.api.json.converters;

import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.json.JsonExchange;

public class JsonExchangeConverter {
    private final JsonCurrencyConverter converter = new JsonCurrencyConverter();

    public JsonExchange toJsonDto(Exchange dto) {
        return JsonExchange.builder()
                .baseCurrency(converter.toJsonDto(dto.getBaseCurrency()))
                .targetCurrency(converter.toJsonDto(dto.getTargetCurrency()))
                .rate(dto.getRate())
                .amount(dto.getAmount())
                .convertedAmount(dto.getConvertedAmount())
                .build();
    }
}
