package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.BaseEntity;

import java.util.Collection;

public interface BaseEntityDao<I extends Comparable<I>, E extends BaseEntity<I>> {
    Collection<E> findAll();

    E findByCode(String code);
}
