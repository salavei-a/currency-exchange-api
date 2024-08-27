package com.asalavei.currencyexchange.api.controllers.filters;

import com.asalavei.currencyexchange.api.exceptions.*;
import com.asalavei.currencyexchange.api.json.JsonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.http.HttpServletResponse.*;

public class ExceptionHandlerFilter extends HttpFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (CEDatabaseException e) {
            writeErrorResponse(response, SC_INTERNAL_SERVER_ERROR, e);
        } catch (CEInvalidInputData e) {
            writeErrorResponse(response, SC_BAD_REQUEST, e);
        } catch (CENotFoundException e) {
            writeErrorResponse(response, SC_NOT_FOUND, e);
        } catch (CEAlreadyExists e) {
            writeErrorResponse(response, SC_CONFLICT, e);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int statusCode, CERuntimeException e) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter writer = response.getWriter()) {
                writer.write(objectMapper.writeValueAsString(new JsonMessage(e.getMessage())));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
