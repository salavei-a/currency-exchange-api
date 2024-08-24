package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;

public class CurrenciesServlet extends BaseServlet<JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    public CurrenciesServlet() {
        super(new JsonCurrencyConverter(), new CurrencyService(new EntityCurrencyConverter(), new JdbcCurrencyDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(service.findAll());
        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonCurrencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        JsonCurrency requestJsonCurrency = JsonCurrency.builder()
                .code(request.getParameter("code"))
                .name(request.getParameter("name"))
                .sign(request.getParameter("sign"))
                .build();

        validate(requestJsonCurrency);

        Currency currency = service.create(converter.toDto(requestJsonCurrency));
        JsonCurrency responseJsonCurrency = converter.toJsonDto(currency);

        writeJsonResponse(response, HttpServletResponse.SC_CREATED, responseJsonCurrency);
    }
}
