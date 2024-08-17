package com.asalavei.currencyexchange.api.dto;

public abstract class BaseDto<I> implements Dto {
    private final I id;

    protected BaseDto(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }
}
