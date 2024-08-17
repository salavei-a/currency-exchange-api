package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dbaccess.dao.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import com.asalavei.currencyexchange.api.services.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeService service;
    private final JsonExchangeConverter converter;

    public ExchangeServlet() {
        this.service = new ExchangeService(
                new CurrencyService(new JdbcCurrencyDao(), new EntityCurrencyConverter()),
                new ExchangeRateService(new JdbcExchangeRateDao(new JdbcCurrencyDao()), new EntityExchangeRateConverter()),
                "USD");
        this.converter = new JsonExchangeConverter();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amountParam = request.getParameter("amount");

        if (baseCurrencyCode == null || baseCurrencyCode.length() != 3 ||
            targetCurrencyCode == null || targetCurrencyCode.length() != 3 ||
            amountParam == null || amountParam.isEmpty()) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Required form field is missing. The code for each currency in the pair must contain 3 characters.",
                    null);
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountParam).setScale(2, RoundingMode.HALF_UP);
            JsonExchange jsonExchange = converter.toJsonDto(service.exchange(baseCurrencyCode, targetCurrencyCode, amount));

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonExchange);
        } catch (NumberFormatException e) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid amount format.", null);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    protected <T> void writeJsonResponse(HttpServletResponse response, int statusCode, String errorMessage, T responseObject) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    writer.write(String.format("{\"message\":\"%s\"}", errorMessage));
                } else if (responseObject != null) {
                    writer.write(objectMapper.writeValueAsString(responseObject));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
