package com.asalavei.currencyexchange.api.dbaccess.dao;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

public interface CurrencyDao extends BaseEntityDao<Integer, EntityCurrency> {
    EntityCurrency findById(Integer id);

    EntityCurrency findByCode(String code);

    Integer getIdByCode(String code);
}
