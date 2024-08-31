package com.asalavei.currencyexchange.api.exceptions;

/**
 * Exception thrown when a database error occurs
 */
public class CEDatabaseException extends CERuntimeException {

    public CEDatabaseException(String message) {
        super(message);
    }
}
