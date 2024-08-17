package com.asalavei.currencyexchange.api.dbaccess.entities;

public abstract class BaseEntity<I> implements Entity {
    private I id;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
