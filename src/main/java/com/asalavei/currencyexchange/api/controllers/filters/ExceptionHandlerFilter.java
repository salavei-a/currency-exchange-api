package com.asalavei.currencyexchange.api.controllers.filters;

import org.slf4j.Logger;
import com.asalavei.currencyexchange.api.exceptions.*;
import com.asalavei.currencyexchange.api.json.JsonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

public class ExceptionHandlerFilter extends HttpFilter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        response.setStatus(statusCode);

        try {
            objectMapper.writeValue(response.getWriter(), new JsonMessage(e.getMessage()));
        } catch (IOException ex) {
            logger.error(ExceptionMessage.ERROR_WRITING_RESPONSE, statusCode, ex);

            throw new CERuntimeException(ExceptionMessage.ERROR_PROCESSING_REQUEST);
        }
    }
}
