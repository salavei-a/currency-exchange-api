package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        validateCurrencyCodesLength(pathInfo);

        String baseCurrencyCode = pathInfo.substring(1, 4);
        String targetCurrencyCode = pathInfo.substring(4, 7);

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);

        ExchangeRate exchangeRate = service.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
        JsonExchangeRate jsonExchangeRate = converter.toJsonDto(exchangeRate);

        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonExchangeRate);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        String rate = getValidatedParamFromBody(request, RATE_PARAM);

        validateCurrencyCodesLength(pathInfo);

        JsonExchangeRate requestJsonExchange = JsonExchangeRate.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(pathInfo.substring(1, 4))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(pathInfo.substring(4, 7))
                        .build())
                .rate(convertToBigDecimal(rate, RATE_PARAM))
                .build();

        validate(requestJsonExchange, RATE_PARAM);

        ExchangeRate dtoExchangeRate = service.update(converter.toDto(requestJsonExchange));
        JsonExchangeRate jsonExchangeRate = converter.toJsonDto(dtoExchangeRate);

        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonExchangeRate);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
