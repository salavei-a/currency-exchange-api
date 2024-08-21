package com.asalavei.currencyexchange.api.exceptions;

public class CENotFoundException extends CERuntimeException {

    public CENotFoundException(String message) {
        this(message, "");
    }

    public CENotFoundException(String message, String info) {
        super(message, info);
    }
}
