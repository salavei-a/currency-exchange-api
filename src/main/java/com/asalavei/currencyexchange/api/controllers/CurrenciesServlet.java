package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;

public class CurrenciesServlet extends BaseServlet<Integer, JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    public CurrenciesServlet() {
        super(new CurrencyService(new JdbcCurrencyDao(), new EntityCurrencyConverter()), new JsonCurrencyConverter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<Currency> dtoCurrencies = service.findAll();
            Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(dtoCurrencies);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrencies);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonCurrency jsonCurrency = JsonCurrency.builder()
                .code(request.getParameter("code"))
                .fullName(request.getParameter("full_name"))
                .sign(request.getParameter("sign"))
                .build();

        if (jsonCurrency.getCode() == null || jsonCurrency.getFullName() == null || jsonCurrency.getSign() == null) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Required form field is missing.", null);
            return;
        }

        Currency dtoCurrency = converter.toDto(jsonCurrency);

        try {
            JsonCurrency savedJsonCurrency = converter.toJsonDto(service.create(dtoCurrency));
            writeJsonResponse(response, HttpServletResponse.SC_CREATED, null, savedJsonCurrency);
        } catch (CEAlreadyExists e) {
            writeJsonResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
