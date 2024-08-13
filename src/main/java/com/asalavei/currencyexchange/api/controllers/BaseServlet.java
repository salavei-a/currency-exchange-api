package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.BaseDto;
import com.asalavei.currencyexchange.api.json.BaseJsonDto;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet<I extends Comparable<I>,
        J extends BaseJsonDto<I>,
        D extends BaseDto<I>,
        C extends JsonDtoConverter<I, J, D>,
        S extends CrudService<I, D>> extends HttpServlet {

    protected final S service;
    protected final C converter;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected BaseServlet() {
        this.service = createService();
        this.converter = createConverter();
    }

    protected abstract S createService();

    protected abstract C createConverter();

    protected <T> void writeJsonResponse(HttpServletResponse response, int statusCode, String errorMessage, T responseObject) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    writer.write(String.format("{\"message\":\"%s\"}", errorMessage));
                } else if (responseObject != null) {
                    writer.write(objectMapper.writeValueAsString(responseObject));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
