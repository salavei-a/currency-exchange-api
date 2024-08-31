package com.asalavei.currencyexchange.api.exceptions;

/**
 * Exception thrown when input data is invalid
 */
public class CEInvalidInputData extends CERuntimeException {

    public CEInvalidInputData(String message) {
        super(message);
    }
}
