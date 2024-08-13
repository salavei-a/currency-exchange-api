package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;

public class ExchangeRatesServlet extends BaseServlet<Integer, JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRatesServlet() {
        super(new ExchangeRateService(), new JsonExchangeRateConverter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<ExchangeRate> dtoExchangeRates = service.findAll();
            Collection<JsonExchangeRate> jsonExchangeRates = converter.toJsonDto(dtoExchangeRates);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonExchangeRates);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
