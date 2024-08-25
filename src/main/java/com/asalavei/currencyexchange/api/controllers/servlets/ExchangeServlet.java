package com.asalavei.currencyexchange.api.controllers.servlets;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcCurrencyDao;
import com.asalavei.currencyexchange.api.dbaccess.repositories.JdbcExchangeRateDao;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.exceptions.CEInvalidInputData;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;
import com.asalavei.currencyexchange.api.json.JsonCurrency;
import com.asalavei.currencyexchange.api.json.JsonExchange;
import com.asalavei.currencyexchange.api.json.converters.JsonExchangeConverter;
import com.asalavei.currencyexchange.api.services.ExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

public class ExchangeServlet extends BaseServlet<JsonExchange, Exchange, JsonExchangeConverter, ExchangeService> {

    public ExchangeServlet() {
        super(new JsonExchangeConverter(), new ExchangeService(new JdbcCurrencyDao(), new JdbcExchangeRateDao(), new EntityCurrencyConverter(), "USD"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String amountParam = request.getParameter("amount");

        if (StringUtils.isBlank(amountParam)) {
            throw new CEInvalidInputData(String.format(ExceptionMessages.INPUT_DATA_MISSING, "amount"));
        }

        JsonExchange requestJsonExchange = JsonExchange.builder()
                .baseCurrency(JsonCurrency.builder()
                        .code(request.getParameter("from"))
                        .build())
                .targetCurrency(JsonCurrency.builder()
                        .code(request.getParameter("to"))
                        .build())
                .amount(convertToBigDecimal(amountParam))
                .build();

        validate(requestJsonExchange);

        Exchange exchangeDto = converter.toDto(requestJsonExchange);
        JsonExchange responseJsonExchange = converter.toJsonDto(service.exchange(exchangeDto));

        writeJsonResponse(response, HttpServletResponse.SC_OK, responseJsonExchange);
    }
}
