package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;

import java.util.Collection;

public interface ExchangeRateDao extends BaseEntityDao<Integer, EntityExchangeRate> {
    @Override
    EntityExchangeRate save(EntityExchangeRate entity);

    @Override
    Collection<EntityExchangeRate> findAll();

    @Override
    EntityExchangeRate findByCode(String code);
}
