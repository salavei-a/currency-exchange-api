package com.asalavei.currencyexchange.api.exceptions;

public class ExceptionMessages {

    public static final String ALREADY_EXISTS = "Failed to save: %s already exists";
    public static final String INPUT_DATA_INVALID = "Required input data is invalid: %s";
    public static final String INPUT_DATA_MISSING = "Required input data is missing: %s";
    public static final String INPUT_DATA_INVALID_OR_MISSING = "Required input data is invalid or missing: %s";
    public static final String CURRENCY_NOT_FOUND = "Currency%s not found";
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate %s/%s not found";
    public static final String EXCHANGE_FAILED = "Exchange operation failed. Cause: %s";

    private ExceptionMessages() {
    }
}
