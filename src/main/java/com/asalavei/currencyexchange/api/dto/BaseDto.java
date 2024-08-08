package com.asalavei.currencyexchange.api.dto;

public interface BaseDto<I extends Comparable<I>> {
    I getId();
}
