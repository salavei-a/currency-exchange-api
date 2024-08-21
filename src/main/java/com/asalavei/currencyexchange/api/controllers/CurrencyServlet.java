package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CurrencyServlet extends BaseServlet<JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    public CurrencyServlet() {
        super(new JsonCurrencyConverter(), new CurrencyService(new EntityCurrencyConverter(), new JdbcCurrencyDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() != 4) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, ExceptionMessages.CURRENCY_CODES_MISSING, null);
            return;
        }

        try {
            Currency dtoCurrency = service.findByCode(pathInfo.substring(1));
            JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrency);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
