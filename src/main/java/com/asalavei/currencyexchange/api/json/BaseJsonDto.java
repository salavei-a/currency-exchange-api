package com.asalavei.currencyexchange.api.json;

public abstract class BaseJsonDto<I extends Comparable<I>> {
    private I id;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
