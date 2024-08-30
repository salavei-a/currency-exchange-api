package com.asalavei.currencyexchange.api.exceptions;

/**
 * Exception thrown when a requested entity is not found
 */
public class CENotFoundException extends CERuntimeException {

    public CENotFoundException(String message) {
        super(message);
    }
}
