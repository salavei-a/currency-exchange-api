package com.asalavei.currencyexchange.api.dbaccess.entities;

public abstract class BaseEntity<T extends Comparable<T>> {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
