package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;

public interface CurrencyRepository extends Repository<EntityCurrency> {
    EntityCurrency findByCode(String code);

    Integer getIdByCode(String code);
}
