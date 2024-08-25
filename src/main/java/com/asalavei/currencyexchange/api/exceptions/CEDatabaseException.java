package com.asalavei.currencyexchange.api.exceptions;

public class CEDatabaseException extends CERuntimeException {

    public CEDatabaseException(String message) {
        this(message, "");
    }

    public CEDatabaseException(String message, String details) {
        super(message, details);
    }

    public CEDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
