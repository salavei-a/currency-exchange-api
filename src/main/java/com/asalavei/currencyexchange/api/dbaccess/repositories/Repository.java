package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;

import java.util.Collection;

public interface Repository<E extends Entity> {
    E save(E entity);

    Collection<E> findAll();
}
