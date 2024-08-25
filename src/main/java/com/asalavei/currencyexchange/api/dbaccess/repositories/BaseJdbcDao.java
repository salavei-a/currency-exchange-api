package com.asalavei.currencyexchange.api.dbaccess.repositories;

import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dbaccess.ConnectionManager;
import com.asalavei.currencyexchange.api.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public abstract class BaseJdbcDao<E extends Entity> {

    protected E save(String query, Object... params) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractEntity(resultSet);
            } else {
                throwException(new CEDatabaseException(ExceptionMessages.SAVE_FAILED, " currency with code " + params[1] + " to the database"),
                               new CENotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND, " required to save the exchange rate in the database"));
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throwException(new CEAlreadyExists(String.format("Currency with the code %s already exists", params[1])),
                               new CEAlreadyExists(String.format("Exchange rate for the currency pair %s/%s already exists", params[2], params[1])));
            }
            throwException(new CEDatabaseException(ExceptionMessages.SAVE_FAILED, " currency with code " + params[1] + ": database operation issue"),
                           new CEDatabaseException(ExceptionMessages.SAVE_FAILED, " exchange rate: database operation issue"));
        }

        throw new CEDatabaseException(ExceptionMessages.ERROR_PROCESSING_REQUEST);
    }

    protected Collection<E> findAll(String query) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return extractEntities(resultSet);
        } catch (SQLException e) {
            throwException(new CEDatabaseException(ExceptionMessages.READ_FAILED, " currencies from the database"),
                           new CEDatabaseException(ExceptionMessages.READ_FAILED, " exchange rates from the database"));
        }

        throw new CEDatabaseException(ExceptionMessages.ERROR_PROCESSING_REQUEST);
    }

    protected E findByCode(String query, String code) {
        return findByCodes(query, code, null);
    }

    protected E findByCodes(String query, String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, baseCurrencyCode);

            if (targetCurrencyCode != null) {
                preparedStatement.setString(2, targetCurrencyCode);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractEntity(resultSet);
            } else {
                throwException(new CENotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND, " with the code " + baseCurrencyCode),
                               new CENotFoundException(String.format(ExceptionMessages.EXCHANGE_RATE_NOT_FOUND, baseCurrencyCode, targetCurrencyCode)));
            }
        } catch (SQLException e) {
            throwException(new CEDatabaseException(ExceptionMessages.READ_FAILED, " currency with the code " + baseCurrencyCode),
                           new CEDatabaseException(ExceptionMessages.READ_FAILED, " exchange rate for the currency pair " + baseCurrencyCode + "/" + targetCurrencyCode));
        }

        throw new CEDatabaseException(ExceptionMessages.ERROR_PROCESSING_REQUEST);
    }

    protected void throwException(CERuntimeException jdbcCurrencyDaoException, CERuntimeException jdbcExchangeRateDaoException) {
        if (this instanceof JdbcCurrencyDao) {
            throw jdbcCurrencyDaoException;
        } else if (this instanceof JdbcExchangeRateDao) {
            throw jdbcExchangeRateDaoException;
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract Collection<E> extractEntities(ResultSet resultSet) throws SQLException;
}
