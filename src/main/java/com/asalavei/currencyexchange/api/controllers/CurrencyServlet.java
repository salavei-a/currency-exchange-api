package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.CrudService;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class CurrencyServlet extends HttpServlet {
    private final CrudService<Integer, Currency> service = new CurrencyService();
    private final JsonDtoConverter<Integer, JsonCurrency, Currency> converter = new JsonCurrencyConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            handleGetCurrencyByCode(response, pathInfo);
        } else {
            handleGetAllCurrencies(response);
        }
    }

    private void handleGetCurrencyByCode(HttpServletResponse response, String pathInfo) {
        String code = pathInfo.substring(1);
        Currency dtoCurrency = service.findByCode(code);
        JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

        writeResponse(response, jsonCurrency);
    }

    private void handleGetAllCurrencies(HttpServletResponse response) {
        Collection<Currency> dtoCurrencies = service.findAll();
        Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(dtoCurrencies);

        writeResponse(response, jsonCurrencies);
    }


    private <T> void writeResponse(HttpServletResponse response, T responseObject) {
        try {
            String jsonCurrencyAsString = objectMapper.writeValueAsString(responseObject);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonCurrencyAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
