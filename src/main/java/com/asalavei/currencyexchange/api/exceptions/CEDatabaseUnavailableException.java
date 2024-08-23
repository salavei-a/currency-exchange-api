package com.asalavei.currencyexchange.api.exceptions;

public class CEDatabaseUnavailableException extends CEDatabaseException {

    public CEDatabaseUnavailableException(String message) {
        super(message);
    }

    public CEDatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
