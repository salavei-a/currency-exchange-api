package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

import java.util.Collection;

public interface CurrencyDao extends BaseEntityDao<Integer, EntityCurrency> {
    @Override
    Collection<EntityCurrency> findAll();
}
