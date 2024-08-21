package com.asalavei.currencyexchange.api.exceptions;

public class CEDatabaseException extends CERuntimeException {

    public CEDatabaseException(String message) {
        super(message);
    }

    public CEDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
