package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.EntityCurrency;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class JdbcCurrencyDao extends BaseJdbcDao<EntityCurrency> implements CurrencyRepository {

    private static final String CURRENCY = "currency with code %s";

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
        return new CEAlreadyExists(String.format(ExceptionMessage.ALREADY_EXISTS, String.format(CURRENCY, params[1])));
    }

    @Override
    protected CERuntimeException createDatabaseException(String operation, Object... params) {
        String details;

        switch (operation) {
            case SAVE_OPERATION -> details = String.format(CURRENCY + " to the database", params[1]);
            case READ_OPERATION -> details = String.format(CURRENCY + " from the database", params[0]);
            case READ_ALL_OPERATION -> details = "currencies from the database";
            default -> throw new CEDatabaseException(ExceptionMessage.ERROR_PROCESSING_REQUEST_TO_DATABASE);
        }

        return new CEDatabaseException(String.format(ExceptionMessage.FAILED_OPERATION, operation, details));
    }
}
