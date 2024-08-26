package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class JdbcCurrencyDao extends BaseJdbcDao<EntityCurrency> implements CurrencyRepository {

    @Override
    public EntityCurrency save(EntityCurrency entity) {
        return save("INSERT INTO currencies (full_name, code, sign) VALUES (?, ?, ?) RETURNING id, full_name, code, sign",
                entity.getName(), entity.getCode(), entity.getSign());
    }

    @Override
    public Collection<EntityCurrency> findAll() {
        return findAll("SELECT id, full_name, code, sign FROM currencies");
    }

    @Override
    public Optional<EntityCurrency> findByCode(String code) {
        return findByCode("SELECT id, full_name, code, sign FROM currencies WHERE code = ?", code);
    }

    @Override
    protected Collection<EntityCurrency> extractEntities(ResultSet resultSet) throws SQLException {
        Collection<EntityCurrency> currencies = new ArrayList<>();
        while (resultSet.next()) {
            currencies.add(extractEntity(resultSet));
        }

        return currencies;
    }

    @Override
    protected EntityCurrency extractEntity(ResultSet resultSet) throws SQLException {
        return EntityCurrency.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("full_name"))
                .code(resultSet.getString("code"))
                .sign(resultSet.getString("sign"))
                .build();
    }

    @Override
    protected CERuntimeException createExceptionForEmptyResultSet(String operation, Object... params) {
        return createDatabaseException(operation, params);
    }

    @Override
    protected CERuntimeException createAlreadyExistsException(Object... params) {
        return new CEAlreadyExists(ExceptionMessages.ALREADY_EXISTS, String.format("currency with code %s", params[1]));

    }

    @Override
    protected CERuntimeException createDatabaseException(String operation, Object... params) {
        String details;

        switch (operation) {
            case "save" -> details = "currency with code " + params[1] + " to the database";
            case "read" ->  details = "currency with code " + params[0] + " from the database";
            case "read all" -> details = "currencies from the database";
            default -> throw new CEDatabaseException(String.format("Failed to %s currency", operation));
        }

        return new CEDatabaseException(String.format("Failed to %s %s", operation, details));
    }
}
