package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonExchangeRate;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeRateConverter;
import com.asalavei.currencyexchange.api.services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

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

        if (pathInfo == null || pathInfo.length() != 7) {
            writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Currency pair codes are missing in the URL request. "
                    + "The code for each currency in the pair must contain 3 characters.", null);
            return;
        }

        try {
            ExchangeRate dtoExchangeRate = service.findByCurrencyPairCodes(pathInfo.substring(1, 4), pathInfo.substring(4, 7));
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

        if (pathInfo == null || pathInfo.length() != 7) {
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
            ExchangeRate dtoExchangeRate = service.updateRate(pathInfo.substring(1, 4), pathInfo.substring(4, 7), new BigDecimal(rateParam));
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
