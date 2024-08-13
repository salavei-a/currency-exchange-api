package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExchangeRateServlet extends BaseServlet<Integer, JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    protected ExchangeRateServlet() {
        super(new ExchangeRateService(), new JsonExchangeRateConverter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
