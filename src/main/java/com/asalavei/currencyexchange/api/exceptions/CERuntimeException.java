package com.asalavei.currencyexchange.api.exceptions;

public class CERuntimeException extends RuntimeException {

    public CERuntimeException(String message) {
        super(message);
    }

    public CERuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
