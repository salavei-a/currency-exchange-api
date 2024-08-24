package com.asalavei.currencyexchange.api.exceptions;

public class ExceptionMessages {

    public static final String DATABASE_UNAVAILABLE = "Database unavailable";
    public static final String SAVE_FAILED = "Failed to save. Please check your input data";
    public static final String INPUT_DATA_INVALID_OR_MISSING = "Required input data is invalid or missing: %s";
    public static final String INPUT_DATA_INVALID = "Required input data is invalid: %s";
    public static final String INPUT_DATA_MISSING = "Required input data is missing: %s";
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate for this currency pair not found";
    public static final String CURRENCY_NOT_FOUND = "Currency%s not found";

    private ExceptionMessages() {
    }
}
