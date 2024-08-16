package com.asalavei.currencyexchange.api.controllers;

import com.asalavei.currencyexchange.api.dto.BaseDto;
import com.asalavei.currencyexchange.api.json.BaseJsonDto;
import com.asalavei.currencyexchange.api.json.converters.JsonDtoConverter;
import com.asalavei.currencyexchange.api.services.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseServlet<I extends Comparable<I>,
        J extends BaseJsonDto<I>,
        D extends BaseDto<I>,
        C extends JsonDtoConverter<I, J, D>,
        S extends CrudService<I, D>> extends HttpServlet {

    protected final S service;
    protected final C converter;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected BaseServlet(S service, C converter) {
        this.service = service;
        this.converter = converter;
    }

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

    protected Map<String, String> getFormParameters(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder body = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            if (body.length() == 0) {
                return new HashMap<>();
            }

            String[] pairs = body.toString().split("&");
            Map<String, String> params = new HashMap<>();

            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);

                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";

                params.put(key, value);
            }

            return params;
        } catch (IOException e) {
            throw new RuntimeException("Error reading form parameters.", e);
        }
    }
}
