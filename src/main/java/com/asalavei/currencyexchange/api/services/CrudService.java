package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dto.Dto;

import java.util.Collection;

public interface CrudService<D extends Dto> extends Service {
    D create(D dto);

    Collection<D> findAll();
}
