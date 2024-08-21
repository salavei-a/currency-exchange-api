package com.asalavei.currencyexchange.api.exceptions;

public class CEDatabaseUnavailableException extends CERuntimeException {

    public CEDatabaseUnavailableException(String message) {
        super(message);
    }

    public CEDatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
