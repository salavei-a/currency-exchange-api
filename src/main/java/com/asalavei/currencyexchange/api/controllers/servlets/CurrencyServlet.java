package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CurrencyServlet extends BaseServlet<JsonCurrency, Currency, JsonCurrencyConverter, CurrencyService> {

    public CurrencyServlet() {
        super(new JsonCurrencyConverter(), new CurrencyService(new EntityCurrencyConverter(), new JdbcCurrencyDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getPathInfo().substring(1);

        validateCurrencyCode(code);

        Currency currency = service.findByCode(code);
        writeJsonResponse(response, HttpServletResponse.SC_OK, converter.toJsonDto(currency));
    }
}
