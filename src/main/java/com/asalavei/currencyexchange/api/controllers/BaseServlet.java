package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.Dto;
import com.asalavei.currencyexchange.api.exceptions.CEInvalidInputData;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonDto;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseServlet<
        J extends JsonDto,
        D extends Dto,
        C extends JsonDtoConverter<J, D>,
        S extends Service> extends HttpServlet {

    protected final C converter;
    protected final S service;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> currencyCodes;

    static {
        currencyCodes = Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
    }

    protected BaseServlet(C converter, S service) {
        this.converter = converter;
        this.service = service;
    }

    protected void validate(JsonCurrency jsonCurrency) {
        String code = jsonCurrency.getCode();
        String name = jsonCurrency.getName();
        String sign = jsonCurrency.getSign();

        if (StringUtils.isBlank(code)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "code"));
        }

        if (StringUtils.isBlank(name)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "name"));
        }

        if (StringUtils.isBlank(sign)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "sign"));
        }

        validateCurrencyCode(code);
    }

    protected void validate(JsonExchange jsonExchange) {
        validate(JsonExchangeRate.builder().
                baseCurrency(jsonExchange.getBaseCurrency())
                .targetCurrency(jsonExchange.getTargetCurrency())
                .rate(jsonExchange.getAmount())
                .build());
    }

    protected void validate(JsonExchangeRate jsonExchangeRate) {
        String baseCurrencyCode = jsonExchangeRate.getBaseCurrency().getCode();
        String targetCurrencyCode = jsonExchangeRate.getTargetCurrency().getCode();

        if (StringUtils.isBlank(baseCurrencyCode) || StringUtils.isBlank(targetCurrencyCode)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "currency code"));
        }

        if (BigDecimal.ZERO.compareTo(jsonExchangeRate.getRate()) > 0) {
            if (this instanceof ExchangeServlet) {
                throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_INVALID, "amount must be a positive number"));
            } throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_INVALID, "rate must be a positive number"));
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    protected void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new CEInvalidInputData("The currency code must be 3 letters");
        }

        if (!currencyCodes.contains(code)) {
            throw new CEInvalidInputData("The currency code must follow the ISO 4217 standard");
        }
    }

    protected <T> void writeJsonResponse(HttpServletResponse response, int statusCode, T responseObject) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(objectMapper.writeValueAsString(responseObject));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static BigDecimal convertToBigDecimal(String rateParameter) {
        try {
            return new BigDecimal(rateParameter);
        } catch (NumberFormatException e) {
            throw new CEInvalidInputData("Invalid rate format");
        }
    }
}
