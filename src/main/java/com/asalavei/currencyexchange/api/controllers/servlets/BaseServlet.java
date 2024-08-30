package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dto.Dto;
import com.asalavei.currencyexchange.api.exceptions.CEInvalidInputData;
import com.asalavei.currencyexchange.api.exceptions.CERuntimeException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessage;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonDto;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class BaseServlet<
        J extends JsonDto,
        D extends Dto,
        C extends JsonDtoConverter<J, D>,
        S extends Service> extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BaseServlet.class.getName());

    protected final C converter;
    protected final S service;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> currencyCodes;

    protected static final String RATE_PARAM = "rate";
    protected static final String AMOUNT_PARAM = "amount";

    static {
        currencyCodes = Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
    }

    protected BaseServlet(C converter, S service) {
        this.converter = converter;
        this.service = service;
    }

    protected <T> void writeJsonResponse(HttpServletResponse response, int statusCode, T responseObject) {
        response.setStatus(statusCode);

        try {
            objectMapper.writeValue(response.getWriter(), responseObject);
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format(ExceptionMessage.ERROR_WRITING_RESPONSE, statusCode), e);
            throw new CERuntimeException(ExceptionMessage.ERROR_PROCESSING_REQUEST);
        }
    }

    protected void validate(JsonCurrency jsonCurrency) {
        String code = jsonCurrency.getCode();
        String name = jsonCurrency.getName();
        String sign = jsonCurrency.getSign();

        if (StringUtils.isBlank(code)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_MISSING, "code"));
        }

        if (StringUtils.isBlank(name)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_MISSING, "name"));
        }

        if (StringUtils.isBlank(sign)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_MISSING, "sign"));
        }

        validateCurrencyCode(code);
    }

    protected void validate(JsonExchange jsonExchange, String param) {
        validate(toJsonExchangeRate(jsonExchange), param);
    }

    protected JsonExchangeRate toJsonExchangeRate(JsonExchange jsonExchange) {
        return JsonExchangeRate.builder().
                baseCurrency(jsonExchange.getBaseCurrency())
                .targetCurrency(jsonExchange.getTargetCurrency())
                .rate(jsonExchange.getAmount())
                .build();
    }

    protected void validate(JsonExchangeRate jsonExchangeRate, String param) {
        String baseCurrencyCode = jsonExchangeRate.getBaseCurrency().getCode();
        String targetCurrencyCode = jsonExchangeRate.getTargetCurrency().getCode();

        if (StringUtils.isBlank(baseCurrencyCode) || StringUtils.isBlank(targetCurrencyCode)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_MISSING, "currency code"));
        }

        if (BigDecimal.ZERO.compareTo(jsonExchangeRate.getRate()) > 0) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_INVALID, param + " must be a positive number"));
        }

        validateCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
    }

    protected void validateCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_INVALID, "base and target currencies are the same"));
        }
    }

    protected void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new CEInvalidInputData("The currency code must be 3 letters");
        }

        if (!currencyCodes.contains(code)) {
            throw new CEInvalidInputData("The currency code must follow the ISO 4217 standard");
        }
    }

    protected void validateCurrencyCodesLength(String pathInfo) {
        if (pathInfo.length() != 7) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_INVALID_OR_MISSING, "currency codes"));
        }
    }

    protected String getValidatedParam(HttpServletRequest request, String param) {
        return validateParamNotBlank(request.getParameter(param), param);
    }

    protected String getValidatedParamFromBody(HttpServletRequest request, String param) {
        try {
            String requestBody = request.getReader().readLine();

            if (StringUtils.isBlank(requestBody) || StringUtils.countMatches(requestBody, "=") != 1 ||
                !requestBody.contains(param + "=")) {
                throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_INVALID_OR_MISSING, param));
            }

            return validateParamNotBlank(requestBody.replace(param + "=", ""), param);

        } catch (IOException e) {
            throw new CEInvalidInputData(String.format("Failed to read request body for parameter: %s", param));
        }
    }

    protected String validateParamNotBlank(String paramValue, String param) {
        if (StringUtils.isBlank(paramValue)) {
            throw new CEInvalidInputData(String.format(ExceptionMessage.INPUT_DATA_MISSING, param));
        }

        return paramValue;
    }

    protected static BigDecimal convertToBigDecimal(String value, String param) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new CEInvalidInputData(String.format("Invalid %s format", param));
        }
    }
}
