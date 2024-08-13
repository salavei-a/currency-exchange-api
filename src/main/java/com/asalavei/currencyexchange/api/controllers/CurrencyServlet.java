package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CurrencyServlet extends BaseServlet<Integer, JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    protected CurrencyServlet() {
        super(new CurrencyService(), new JsonCurrencyConverter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Currency code is missing in the URL request.", null);
            return;
        }

        try {
            Currency dtoCurrency = service.findByCode(pathInfo.substring(1));
            JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrency);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
