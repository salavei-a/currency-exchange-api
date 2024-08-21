package com.asalavei.currencyexchange.api.exceptions;

public class ExceptionMessages {
    public static final String DATABASE_UNAVAILABLE = "Database unavailable";
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate for this currency pair not found";
    public static final String CURRENCY_NOT_FOUND = "Currency%s not found";
    public static final String CURRENCY_CODES_MISSING = "Currency pair codes are missing in the URL request. The code for each currency in the pair must contain 3 characters";

    private ExceptionMessages() {
    }
}
