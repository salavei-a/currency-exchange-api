package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dto.Dto;

import java.util.Collection;

/**
 * Service for CRUD operations
 *
 * @param <D> the type of {@link Dto}
 */
public interface CrudService<D extends Dto> extends Service {
    D create(D dto);

    Collection<D> findAll();
}
