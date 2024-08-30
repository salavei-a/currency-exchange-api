package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeConverter;
import com.asalavei.currencyexchange.api.services.ExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling GET requests to get the amount after currency exchange
 */
public class ExchangeServlet extends BaseServlet<JsonExchange, Exchange, JsonExchangeConverter, ExchangeService> {

    public ExchangeServlet() {
        super(new JsonExchangeConverter(), new ExchangeService(new JdbcExchangeRateDao(), new EntityExchangeRateConverter(), "USD"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String amount = getValidatedParam(request, AMOUNT_PARAM);

        JsonExchange requestJsonExchange = JsonExchange.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(request.getParameter("from"))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(request.getParameter("to"))
                        .build())
                .amount(convertToBigDecimal(amount, AMOUNT_PARAM))
                .build();

        validate(requestJsonExchange, AMOUNT_PARAM);

        Exchange exchangeDto = converter.toDto(requestJsonExchange);
        JsonExchange responseJsonExchange = converter.toJsonDto(service.exchange(exchangeDto));

        writeJsonResponse(response, HttpServletResponse.SC_OK, responseJsonExchange);
    }
}
