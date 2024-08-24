package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.*;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class ExchangeRatesServlet extends BaseServlet<JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRatesServlet() {
        super(new JsonExchangeRateConverter(), new ExchangeRateService(new EntityExchangeRateConverter(), new JdbcExchangeRateDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Collection<JsonExchangeRate> jsonExchangeRates = converter.toJsonDto(service.findAll());
        writeJsonResponse(response, HttpServletResponse.SC_OK, jsonExchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String rateParameter = request.getParameter("rate");

        if (StringUtils.isBlank(rateParameter)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "rate"));
        }

        JsonExchangeRate requestJsonExchange = JsonExchangeRate.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(request.getParameter("baseCurrencyCode"))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(request.getParameter("targetCurrencyCode"))
                        .build())
                .rate(convertToBigDecimal(rateParameter))
                .build();

        validate(requestJsonExchange);

        ExchangeRate exchangeRateDto = converter.toDto(requestJsonExchange);
        JsonExchangeRate responseJsonExchange = converter.toJsonDto(service.create(exchangeRateDto));

        writeJsonResponse(response, HttpServletResponse.SC_CREATED, responseJsonExchange);
    }
}
