package com.asalavei.currencyexchange.api.exceptions;

/**
 * Exception thrown when an entity already exists
 */
public class CEAlreadyExists extends CERuntimeException {

    public CEAlreadyExists(String message) {
        super(message);
    }
}
