package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dbaccess.ConnectionManager;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public abstract class BaseJdbcDao<E extends Entity> {

    protected static final String SAVE_OPERATION = "save";
    protected static final String READ_OPERATION = "read";
    protected static final String READ_ALL_OPERATION = "read all";
    protected static final String UPDATE_OPERATION = "update";

    protected E save(String query, Object... params) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractEntity(resultSet);
            } else {
                throw createExceptionForEmptyResultSet(SAVE_OPERATION, params);
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw createAlreadyExistsException(params);
            }
            throw createDatabaseException(SAVE_OPERATION, params);
        }
    }

    protected Collection<E> findAll(String query) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return extractEntities(resultSet);
        } catch (SQLException e) {
            throw createDatabaseException(READ_ALL_OPERATION);
        }
    }

    protected Optional<E> findByCode(String query, String code) {
        return findByCodes(query, code, null);
    }

    protected Optional<E> findByCodes(String query, String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, baseCurrencyCode);

            if (targetCurrencyCode != null) {
                preparedStatement.setString(2, targetCurrencyCode);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            throw createDatabaseException(READ_OPERATION, baseCurrencyCode, targetCurrencyCode);
        }

        return Optional.empty();
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    protected abstract Collection<E> extractEntities(ResultSet resultSet) throws SQLException;

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract CERuntimeException createExceptionForEmptyResultSet(String operation, Object ... params);

    protected abstract CERuntimeException createAlreadyExistsException(Object ... params);

    protected abstract CERuntimeException createDatabaseException(String operation, Object ... params);
}
