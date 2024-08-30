package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;

import java.util.Collection;

/**
 * Provides basic CRUD operations for managing entities
 *
 * @param <E> the type of {@link Entity}
 */
public interface Repository<E extends Entity> {
    E save(E entity);

    Collection<E> findAll();
}
