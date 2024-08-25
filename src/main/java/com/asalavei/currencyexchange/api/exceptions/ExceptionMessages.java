package com.asalavei.currencyexchange.api.exceptions;

public class ExceptionMessages {

    public static final String SAVE_FAILED = "Failed to save%s";
    public static final String READ_FAILED = "Failed to read%s";
    public static final String INPUT_DATA_INVALID = "Required input data is invalid: %s";
    public static final String INPUT_DATA_MISSING = "Required input data is missing: %s";
    public static final String INPUT_DATA_INVALID_OR_MISSING = "Required input data is invalid or missing: %s";
    public static final String CURRENCY_NOT_FOUND = "Currency%s not found";
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate for the currency pair not found: %s/%s";
    public static final String ERROR_PROCESSING_REQUEST = "An error occurred while processing the request to the database";

    private ExceptionMessages() {
    }
}
