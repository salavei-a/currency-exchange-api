package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dbaccess.dao.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRateServlet extends BaseServlet<Integer, JsonExchangeRate, ExchangeRate, JsonExchangeRateConverter, ExchangeRateService> {

    public ExchangeRateServlet() {
        super(new ExchangeRateService(new JdbcExchangeRateDao(new JdbcCurrencyDao()), new EntityExchangeRateConverter()), new JsonExchangeRateConverter());
    }

    private final CurrencyService currencyService = new CurrencyService(new JdbcCurrencyDao(), new EntityCurrencyConverter());

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

        if (pathInfo == null || "/".equals(pathInfo) || pathInfo.length() != 7) {
            writeJsonResponse(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Currency pair codes are missing in the URL request. The code for each currency in the pair must contain 3 characters.",
                    null);
        }

        try {
            Currency dtoBaseCurrency = currencyService.findByCode(pathInfo.substring(1, 4));
            Currency dtoTargetCurrency = currencyService.findByCode(pathInfo.substring(4, 7));

            ExchangeRate dtoExchangeRate = service.findByCurrencyPair(dtoBaseCurrency.getId(), dtoTargetCurrency.getId());
            JsonExchangeRate jsonExchangeRate = converter.toJsonDto(dtoExchangeRate);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonExchangeRate);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        Map<String, String> params = getFormParameters(request);
        String rateParam = params.get("rate");

        if (pathInfo == null || "/".equals(pathInfo) || pathInfo.length() != 7) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Currency pair codes are missing in the URL request. The code for each currency in the pair must contain 3 characters.",
                    null);
            return;
        }

        if (rateParam == null || rateParam.isEmpty()) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Rate value is missing.",
                    null);
            return;
        }

        try {
            BigDecimal rate = new BigDecimal(rateParam);
            Integer idBaseCurrency = currencyService.getIdByCode(pathInfo.substring(1, 4));
            Integer idTargetCurrency = currencyService.getIdByCode(pathInfo.substring(4, 7));

            ExchangeRate dtoExchangeRate = service.update(rate, idBaseCurrency, idTargetCurrency);
            JsonExchangeRate jsonExchangeRate = converter.toJsonDto(dtoExchangeRate);

            writeJsonResponse(response, HttpServletResponse.SC_OK, null, jsonExchangeRate);
        } catch (NumberFormatException e) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid rate format.", null);
        } catch (CENotFoundException e) {
            writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), null);
        } catch (CEDatabaseUnavailableException e) {
            writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
