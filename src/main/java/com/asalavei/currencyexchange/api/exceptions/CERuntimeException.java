package com.asalavei.currencyexchange.api.exceptions;

/**
 * Base exception for all custom runtime exceptions in the currency exchange API
 */
public class CERuntimeException extends RuntimeException {

    public CERuntimeException(String message) {
        super(message);
    }
}
