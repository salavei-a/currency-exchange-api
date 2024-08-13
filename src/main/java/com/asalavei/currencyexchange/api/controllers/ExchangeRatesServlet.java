package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;

public class ExchangeRatesServlet extends BaseServlet<Integer, JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRatesServlet() {
        super(new ExchangeRateService(), new JsonExchangeRateConverter());
    }
}
