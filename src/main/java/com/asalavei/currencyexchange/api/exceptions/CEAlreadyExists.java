package com.asalavei.currencyexchange.api.exceptions;

public class CEAlreadyExists extends CERuntimeException {

    public CEAlreadyExists(String message, String details) {
        super(message, details);
    }
}
