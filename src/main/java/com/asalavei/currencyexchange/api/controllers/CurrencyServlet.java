package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.CrudService;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;

public class CurrencyServlet extends BaseServlet {
    private final CrudService<Integer, Currency> service = new CurrencyService();
    private final JsonDtoConverter<Integer, JsonCurrency, Currency> converter = new JsonCurrencyConverter();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        if ("/".equals(pathInfo)) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Currency code is missing in the URL request.", null);
            return;
        }

        try {
            if (pathInfo == null) {
                handleGetAllCurrencies(response);
            } else {
                handleGetCurrencyByCode(response, pathInfo);
            }
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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

    private void handleGetCurrencyByCode(HttpServletResponse response, String pathInfo) {
        String code = pathInfo.substring(1);
        Currency dtoCurrency = service.findByCode(code);
        JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

        writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrency);
    }

    private void handleGetAllCurrencies(HttpServletResponse response) {
        Collection<Currency> dtoCurrencies = service.findAll();
        Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(dtoCurrencies);

        writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonCurrencies);
    }
}
