package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;
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
        try {
            Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(service.findAll());
            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrencies);
        } catch (CEDatabaseException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String name = request.getParameter("full_name");
        String sign = request.getParameter("sign");

        if (code == null || code.length() != 3 ||
            name == null || name.isEmpty() ||
            sign == null || sign.isEmpty()) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, ExceptionMessages.INPUT_DATA_MISSING, null);
            return;
        }

        Currency dtoCurrency = Currency.builder()
                .name(name)
                .code(code)
                .sign(sign)
                .build();

        try {
            JsonCurrency savedJsonCurrency = converter.toJsonDto(service.create(dtoCurrency));
            writeJsonResponse(response, HttpServletResponse.SC_CREATED, null, savedJsonCurrency);
        } catch (CEAlreadyExists e) {
            writeJsonResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage(), null);
        } catch (CEDatabaseException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
