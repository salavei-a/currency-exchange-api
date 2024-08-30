package com.asalavei.currencyexchange.api.json;

/**
 * JSON DTO for message data
 */
public class JsonMessage implements JsonDto {

    private final String message;

    public JsonMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
