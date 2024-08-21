package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeConverter;
import com.asalavei.currencyexchange.api.services.ExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeServlet extends BaseServlet<JsonExchange, Exchange, JsonExchangeConverter, ExchangeService> {

    public ExchangeServlet() {
        super(new JsonExchangeConverter(), new ExchangeService(new JdbcCurrencyDao(), new JdbcExchangeRateDao(), new EntityCurrencyConverter(),"USD"));
    }

    @Override
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
        } catch (CEDatabaseException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
