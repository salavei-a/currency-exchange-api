package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dto.BaseDto;

import java.util.Collection;

public interface CrudService<I extends Comparable<I>, D extends BaseDto<I>> {
    void save(D dto);

    Collection<D> findAll();

    D findByCode(String code);
}
