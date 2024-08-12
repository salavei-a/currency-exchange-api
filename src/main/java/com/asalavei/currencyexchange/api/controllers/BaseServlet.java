package com.asalavei.currencyexchange.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

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
