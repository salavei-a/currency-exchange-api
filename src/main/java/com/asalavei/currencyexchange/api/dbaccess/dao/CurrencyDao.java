package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

import java.util.Collection;

public interface CurrencyDao extends BaseEntityDao<Integer, EntityCurrency> {
    @Override
    EntityCurrency save(EntityCurrency entity);

    @Override
    Collection<EntityCurrency> findAll();

    @Override
    EntityCurrency findByCode(String code);
}
