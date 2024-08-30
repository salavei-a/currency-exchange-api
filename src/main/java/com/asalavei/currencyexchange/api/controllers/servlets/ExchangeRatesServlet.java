package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExchangeRatesServlet extends BaseServlet<JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRatesServlet() {
        super(new JsonExchangeRateConverter(), new ExchangeRateService(new EntityExchangeRateConverter(), new JdbcExchangeRateDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        writeJsonResponse(response, HttpServletResponse.SC_OK, converter.toJsonDto(service.findAll()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String rate = getValidatedParam(request, RATE_PARAM);

        JsonExchangeRate requestJsonExchange = JsonExchangeRate.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(request.getParameter("baseCurrencyCode"))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(request.getParameter("targetCurrencyCode"))
                        .build())
                .rate(convertToBigDecimal(rate, RATE_PARAM))
                .build();

        validate(requestJsonExchange, RATE_PARAM);

        ExchangeRate exchangeRateDto = converter.toDto(requestJsonExchange);
        JsonExchangeRate responseJsonExchange = converter.toJsonDto(service.create(exchangeRateDto));

        writeJsonResponse(response, HttpServletResponse.SC_CREATED, responseJsonExchange);
    }
}
