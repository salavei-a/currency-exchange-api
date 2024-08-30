package com.asalavei.currencyexchange.api.json;

/**
 * Base JSON DTO with an identifier
 */
public abstract class BaseJsonDto<I> implements JsonDto {

    private I id;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
