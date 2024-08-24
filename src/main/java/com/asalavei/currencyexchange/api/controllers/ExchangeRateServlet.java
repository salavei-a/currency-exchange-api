package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEInvalidInputData;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ExchangeRateServlet extends BaseServlet<JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRateServlet() {
        super(new JsonExchangeRateConverter(), new ExchangeRateService(new EntityExchangeRateConverter(), new JdbcExchangeRateDao()));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            this.doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        if (pathInfo.length() != 7) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_INVALID_OR_MISSING, "currency codes"));
        }

        String baseCurrencyCode = pathInfo.substring(1, 4);
        String targetCurrencyCode = pathInfo.substring(4, 7);

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);

        ExchangeRate exchangeRate = service.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
        JsonExchangeRate jsonExchangeRate = converter.toJsonDto(exchangeRate);

        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonExchangeRate);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo.length() != 7) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_INVALID_OR_MISSING, "currency codes"));
        }

        String parameter = request.getReader().readLine();

        if (StringUtils.isBlank(parameter) || !parameter.contains("rate=")) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_INVALID, "rate"));
        }

        String rate = parameter.split("=")[1];

        JsonExchangeRate requestJsonExchange = JsonExchangeRate.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(pathInfo.substring(1, 4))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(pathInfo.substring(4, 7))
                        .build())
                .rate(convertToBigDecimal(rate))
                .build();

        validate(requestJsonExchange);

        ExchangeRate dtoExchangeRate = service.update(converter.toDto(requestJsonExchange));
        JsonExchangeRate jsonExchangeRate = converter.toJsonDto(dtoExchangeRate);

        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonExchangeRate);
    }
}
