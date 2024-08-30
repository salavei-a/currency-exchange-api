package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling GET and POST requests to get all currencies and create a currency in the database
 */
public class CurrenciesServlet extends BaseServlet<JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    public CurrenciesServlet() {
        super(new JsonCurrencyConverter(), new CurrencyService(new EntityCurrencyConverter(), new JdbcCurrencyDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        writeJsonResponse(response, HttpServletResponse.SC_OK, converter.toJsonDto(service.findAll()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        JsonCurrency requestJsonCurrency = JsonCurrency.builder()
                .code(request.getParameter("code"))
                .name(request.getParameter("name"))
                .sign(request.getParameter("sign"))
                .build();

        validate(requestJsonCurrency);

        Currency currency = service.create(converter.toDto(requestJsonCurrency));

        writeJsonResponse(response, HttpServletResponse.SC_CREATED, converter.toJsonDto(currency));
    }
}
