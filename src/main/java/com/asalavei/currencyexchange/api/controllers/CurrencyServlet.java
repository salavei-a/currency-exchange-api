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
import java.util.Collection;

public class CurrencyServlet extends HttpServlet {
    private final CrudService<Integer, Currency> service = new CurrencyService();
    private final JsonDtoConverter<Integer, JsonCurrency, Currency> converter = new JsonCurrencyConverter();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String code = pathInfo.substring(1);

            Currency dtoCurrency = service.findByCode(code);
            JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonCurrencyAsString = objectMapper.writeValueAsString(jsonCurrency);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonCurrencyAsString);
        } else {
            Collection<Currency> dtoCurrencies = service.findAll();
            Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(dtoCurrencies);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonCurrencyAsString = objectMapper.writeValueAsString(jsonCurrencies);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonCurrencyAsString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
