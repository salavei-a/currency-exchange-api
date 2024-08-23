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
                if (this instanceof JdbcExchangeRateDao) {
                    throw new CENotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND, " required to save the exchange rate");
                } else {
                    throw new CEDatabaseException(ExceptionMessages.SAVE_FAILED);
                }
            }
        } catch (NoClassDefFoundError e) {
            throw new CEDatabaseException(ExceptionMessages.SAVE_FAILED);
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                if (this instanceof JdbcCurrencyDao) {
                    throw new CEAlreadyExists(String.format("Currency with the code '%s' already exists", params[1]));
                } else if (this instanceof JdbcExchangeRateDao) {
                    throw new CEAlreadyExists(String.format("Exchange rate from %s to %s already exists", params[2], params[1]));
                }
            }
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
        }
    }

    protected Collection<E> findAll(String query) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return extractEntities(resultSet);
        } catch (NoClassDefFoundError e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE);
        } catch (SQLException e) {
            throw new CEDatabaseUnavailableException(ExceptionMessages.DATABASE_UNAVAILABLE, e);
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
