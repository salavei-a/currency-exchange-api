package com.asalavei.currencyexchange.api.controllers.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, X-Requested-With");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, PATCH, POST, DELETE");
        response.addHeader("Access-Control-Allow-Credentials", "true");

        super.doFilter(request, response, chain);
    }
}
