package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.Collection;

public class ExchangeRatesServlet extends BaseServlet<JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRatesServlet() {
        super(new JsonExchangeRateConverter(), new ExchangeRateService(new EntityExchangeRateConverter(), new JdbcExchangeRateDao()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<JsonExchangeRate> jsonExchangeRates = converter.toJsonDto(service.findAll());
            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonExchangeRates);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rateParam = request.getParameter("rate");

        if (baseCurrencyCode == null || baseCurrencyCode.length() != 3 ||
            targetCurrencyCode == null || targetCurrencyCode.length() != 3 ||
            rateParam == null || rateParam.isEmpty()) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Required form field is missing. "
                    + "The code for each currency in the pair must contain 3 characters.", null);
            return;
        }

        try {
            JsonExchangeRate savedJsonExchangeRate = converter.toJsonDto(service.create(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rateParam)));
            writeJsonResponse(response, HttpServletResponse.SC_CREATED, null, savedJsonExchangeRate);
        } catch (NumberFormatException e) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid rate format.", null);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEAlreadyExists e) {
            writeJsonResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
