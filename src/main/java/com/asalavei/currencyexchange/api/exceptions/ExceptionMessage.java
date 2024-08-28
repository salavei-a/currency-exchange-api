package com.asalavei.currencyexchange.api.exceptions;

public class ExceptionMessage {

    public static final String INPUT_DATA_INVALID = "Required input data is invalid: %s";
    public static final String INPUT_DATA_MISSING = "Required input data is missing: %s";
    public static final String INPUT_DATA_INVALID_OR_MISSING = "Required input data is invalid or missing: %s";

    public static final String ERROR_WRITING_RESPONSE = "Error writing response for status code: %s";
    public static final String ERROR_PROCESSING_REQUEST = "An error occurred while processing the request";
    public static final String ERROR_PROCESSING_REQUEST_TO_DATABASE = ERROR_PROCESSING_REQUEST + " to the database";
    public static final String DATABASE_UNAVAILABLE = "Database unavailable";

    public static final String CURRENCY_NOT_FOUND = "Currency with code %s not found";
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate %s/%s not found";
    public static final String ALREADY_EXISTS = "Failed to save: %s already exists";
    public static final String FAILED_OPERATION = "Failed to %s %s";
    public static final String EXCHANGE_FAILED = "Exchange operation failed. Cause: %s";

    private ExceptionMessage() {
    }
}
