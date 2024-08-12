package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.exceptions.CEAlreadyExists;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseUnavailableException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.converters.JsonCurrencyConverter;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.CrudService;
import com.asalavei.currencyexchange.api.services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class CurrencyServlet extends HttpServlet {
    private final CrudService<Integer, Currency> service = new CurrencyService();
    private final JsonDtoConverter<Integer, JsonCurrency, Currency> converter = new JsonCurrencyConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeErrorResponse(response, "Currency code is missing in the URL request.");
        }

        try {
            if (pathInfo == null) {
                handleGetAllCurrencies(response);
            } else {
                handleGetCurrencyByCode(response, pathInfo);
            }
        } catch (CENotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeErrorResponse(response, e.getMessage());
        } catch (CEDatabaseUnavailableException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponse(response, e.getMessage());
        }
    }

    private static void writeErrorResponse(HttpServletResponse response, String errorMessage) {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(("application/json"));
            response.setCharacterEncoding("UTF-8");
            writer.write(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonCurrency jsonCurrency = JsonCurrency.builder()
                .code(request.getParameter("code"))
                .fullName(request.getParameter("full_name"))
                .sign(request.getParameter("sign"))
                .build();

        if (jsonCurrency.getCode() == null || jsonCurrency.getFullName() == null || jsonCurrency.getSign() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeErrorResponse(response, "Required form field is missing.");
        }

        Currency dtoCurrency = converter.toDto(jsonCurrency);

        try {
            Currency savedDto = service.create(dtoCurrency);

            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, "Currency created successfully.", savedDto);
        } catch (CEAlreadyExists e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            writeErrorResponse(response, e.getMessage());
        } catch (CEDatabaseUnavailableException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponse(response, e.getMessage());
        }
    }

    private void handleGetCurrencyByCode(HttpServletResponse response, String pathInfo) {
        String code = pathInfo.substring(1);
        Currency dtoCurrency = service.findByCode(code);
        JsonCurrency jsonCurrency = converter.toJsonDto(dtoCurrency);

        writeResponse(response, jsonCurrency);
    }

    private void handleGetAllCurrencies(HttpServletResponse response) {
        Collection<Currency> dtoCurrencies = service.findAll();
        Collection<JsonCurrency> jsonCurrencies = converter.toJsonDto(dtoCurrencies);

        writeResponse(response, jsonCurrencies);
    }

    private <T> void writeResponse(HttpServletResponse response, String message, T responseObject) {
        try {
            String jsonString = objectMapper.writeValueAsString(responseObject);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(message + "\n" + jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private <T> void writeResponse(HttpServletResponse response, T responseObject) {
        try {
            String jsonCurrencyAsString = objectMapper.writeValueAsString(responseObject);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonCurrencyAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
